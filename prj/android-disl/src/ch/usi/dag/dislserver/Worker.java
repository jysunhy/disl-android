package ch.usi.dag.dislserver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.d2j.org.objectweb.asm.ClassReader;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disl.exception.DiSLException;
import ch.usi.dag.disl.util.Constants;

import com.googlecode.dex2jar.Method;
import com.googlecode.dex2jar.reader.DexFileReader;
import com.googlecode.dex2jar.v3.Dex2jar;
import com.googlecode.dex2jar.v3.DexExceptionHandlerImpl;

public class Worker extends Thread {

	private static final boolean debug = Boolean
			.getBoolean(DiSLServer.PROP_DEBUG);

	private static final String PROP_UNINSTR = "dislserver.uninstrumented";
	private static final String uninstrPath =
			System.getProperty(PROP_UNINSTR, null);

	private static final String PROP_INSTR = "dislserver.instrumented";
	private static final String instrPath =
			System.getProperty(PROP_INSTR, null);

	private static final String PROP_ANDROID = "dislsever.android";
	private static final boolean ANDROID = true; // Boolean.getBoolean(PROP_ANDROID);

	private static final boolean EMPTY_INSTR = false;

	// used for replays
	private static final byte[] emptyByteArray = new byte[0];

	private final NetMessageReader sc;
	private final DiSL disl;

	private final AtomicLong instrumentationTime = new AtomicLong();


	Worker(final NetMessageReader sc, final DiSL disl) {
		this.sc = sc;
		this.disl = disl;
	}


	private void instrumentJar(final String jarName) throws IOException,
	FileNotFoundException {
		JarFile jf;
		Enumeration<JarEntry> entryEnum;
		System.out.println("starting rewriting of jar file " + jarName);
		jf = new JarFile(jarName);

		entryEnum = jf.entries();
		final String originalName=jarName.substring(jarName.lastIndexOf("/")+1);
		String writePath;
		writePath = "instrumented_"+originalName;

		final File f = new File(writePath);
		final FileOutputStream fos = new FileOutputStream(f);
		final ZipOutputStream zos = new ZipOutputStream(fos);
		final byte[] buffer = new byte[8192];
		int bytesRead;
		while (entryEnum.hasMoreElements()) {
			final ZipEntry ze = entryEnum.nextElement();
			final String entryName = ze.getName();
			final InputStream is = jf.getInputStream(ze);

			if (entryName.endsWith(".class")) {
				System.out.println("now in class: " + entryName);
				try {
					final ZipEntry nze = new ZipEntry(entryName);
					final ClassReader cr = new ClassReader(is);

					final String className = entryName.substring(0, entryName.lastIndexOf(".class"));
					System.out.println("using className: " + className);

					// instrument it
					final byte[] code = instrument(className, cr.b);

					zos.putNextEntry(nze);
					final ByteArrayInputStream bin = new ByteArrayInputStream(code);
					System.out.println(" Adding to JAR file " + nze.getName());
					while ((bytesRead = bin.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
					zos.closeEntry();
				} catch (final Exception e) {
					e.printStackTrace();
				}

			} else {
				final ZipEntry nze = new ZipEntry(entryName);
				zos.putNextEntry(nze);
				System.out.println("ignored jar entry: " + ze);

				while ((bytesRead = is.read(buffer)) != -1) {
					System.out.println("Read " + bytesRead
							+ " byte(s) from jar file");
					zos.write(buffer, 0, bytesRead);
				}
				zos.closeEntry();
			}
		}
		zos.finish();
		zos.close();
		fos.flush();
		fos.close();
		jf.close();
	}



	@Override
    public void run() {

		try {


				instrumentationLoop();

			sc.close();
		}
		catch (final Throwable e) {
			DiSLServer.reportError(e);
		}
		finally {
			DiSLServer.workerDone(instrumentationTime.get());
		}
	}


	private void instrumentationLoop() throws Exception {

		try {

		while (true) {
			final NetMessage nm = sc.readMessage();

			// communication closed by the client
			if (nm.getControl().length == 0 && nm.getClassCode().length == 0) {
				return;
			}

			byte[] instrClass;

			try {

				final long startTime = System.nanoTime();

				if(ANDROID) {
					final Path fullPath = Paths.get(new String(nm.getControl()));
					final String dexName = fullPath.getFileName().toString();
					final byte[] dexCode = nm.getClassCode();

					// read java class
					final String fileName = dexName;

					if(EMPTY_INSTR) {
						 instrClass = dexCode; // do nothing

					}else{

						//File file = new File(dexName + "-dex2jar.jar");

					    final File origDexFile = File.createTempFile(dexName, "-orig.dex");
					    FileUtils.writeByteArrayToFile(origDexFile, dexCode);


						final File file = File.createTempFile(dexName, "-dex2jar.jar");
						final DexFileReader reader = new DexFileReader(dexCode);

						final DexExceptionHandlerImpl handler = new DexExceptionHandlerImpl().skipDebug(true);

						Dex2jar.from(reader).withExceptionHandler(handler).reUseReg(false)
						.topoLogicalSort(false)
						.skipDebug(true)
						.optimizeSynchronized(false)
						.printIR(false)
						.verbose(false)
						.to(file);

						final Map<Method, Exception> exceptions = handler.getExceptions();
						if (exceptions.size() > 0) {
							//  File errorFile = new File(FilenameUtils.getBaseName(fileName) + "-error.zip");
							final File errorFile = new File(fileName + "-error.zip");
							handler.dumpException(reader, errorFile);
							System.err.println("Detail Error Information in File " + errorFile);
						}

						// Now open jar file, and instrument only the .class files
						// rename instrumented_thejarname.jar
						instrumentJar(file.getAbsolutePath());

						// now call DX on the instrumented jar

						final Class<?> c = Class.forName("com.android.dx.command.Main");
						final java.lang.reflect.Method m = c.getMethod("main", String[].class);

						final File realJar = new File("instrumented_" + file.getName());
						final File outputDex = new File(realJar.getName() + "-jar2dex.dex");

						final List<String> ps = new ArrayList<String>();
						ps.addAll(Arrays.asList("--dex", "--no-strict", "--output=" + outputDex.getCanonicalPath(),
								realJar.getCanonicalPath()));
						System.out.println("call com.android.dx.command.Main.main" + ps);
						m.invoke(null, new Object[] { ps.toArray(new String[0]) });

						// now read the instrumented dex file and pass return it as byte[]
						instrClass = Files.readAllBytes(Paths.get(outputDex.getAbsolutePath()));
					}

				}else{
				   instrClass = instrument(new String(nm.getControl()),
							nm.getClassCode());
				}
				instrumentationTime.addAndGet(System.nanoTime() - startTime);
			}
			catch (final Exception e) {

				// instrumentation error
				// send the client a description of the server-side error

				String errToReport = e.getMessage();

				// during debug send the whole message
				if(debug) {
					final StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					errToReport = sw.toString();
				}

				// error protocol:
				// control contains the description of the server-side error
				// class code is an array of size zero
				final String errMsg = "Instrumentation error for class "
						+ new String(nm.getControl()) + ": " + errToReport;

				sc.sendMessage(new NetMessage(errMsg.getBytes(),
						emptyByteArray));

				throw e;
			}

			NetMessage replyData = null;

			if(instrClass != null) {
				// class was modified - send modified data
				replyData = new NetMessage(emptyByteArray, instrClass);
			}
			else {
				// zero length means no modification
				replyData = new NetMessage(emptyByteArray, emptyByteArray);
			}

			sc.sendMessage(replyData);
			if(ANDROID) {
                break;
            }
		}

		}
		catch (final IOException e) {
			throw new DiSLServerException(e);
		}
	}

	private byte[] instrument(String className, final byte[] origCode)
			throws DiSLServerException, DiSLException {

		// backup for empty class name
		if(className == null || className.isEmpty()) {
			className = UUID.randomUUID().toString();
		}

		// dump uninstrumented
		if (uninstrPath != null) {
			dump(className, origCode, uninstrPath);
		}

		// instrument
		final byte[] instrCode = disl.instrument(origCode);

		// dump instrumented
		if (instrPath != null && instrCode != null) {
			dump(className, instrCode, instrPath);
		}

		return instrCode;
	}

	private void dump(final String className, final byte[] codeAsBytes, final String path)
			throws DiSLServerException {

		try {

			// extract the class name and package name
			final int i = className.lastIndexOf(Constants.PACKAGE_INTERN_DELIM);
			final String onlyClassName = className.substring(i + 1);
			final String packageName = className.substring(0, i + 1);

			// construct path to the class
			final String pathWithPkg = path + File.separator + packageName;

			// create directories
			new File(pathWithPkg).mkdirs();

			// dump the class code
			final FileOutputStream fo = new FileOutputStream(pathWithPkg
					+ onlyClassName + Constants.CLASS_EXT);
			fo.write(codeAsBytes);
			fo.close();
		}
		catch (final FileNotFoundException e) {
			throw new DiSLServerException(e);
		}
		catch (final IOException e) {
			throw new DiSLServerException(e);
		}
	}
}
