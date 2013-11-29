package ch.usi.dag.disl.testtools.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import ch.usi.dag.disl.DiSL;

public class Transformer implements ClassFileTransformer {

	private static DiSL disl = null;
	
    @Override
    public byte [] transform (ClassLoader loader, String className,
        Class <?> classBeingRedefined, ProtectionDomain protectionDomain,
        byte [] classfileBuffer)
        throws IllegalClassFormatException {
    	
    	byte[] instrumentedClass = null;
    	
    	try {
    	
    		// init DiSL
    		if(disl == null) {

    			// false - do not use dynamic bypass
    			disl = new DiSL(false);
    		}
    		
			instrumentedClass = disl.instrument(classfileBuffer);
			
			if(instrumentedClass != null) {
			
				/*
				// print class
				ClassReader cr = new ClassReader(instrumentedClass);
				TraceClassVisitor tcv = new TraceClassVisitor(new PrintWriter(System.out));
				cr.accept(tcv, 0);
				/**/
				
				/*
				// check class
				ClassReader cr2 = new ClassReader(instrumentedClass);
				ClassWriter cw = new ClassWriter(cr2, ClassWriter.COMPUTE_MAXS);
				cr2.accept(new CheckClassAdapter(cw), 0);
				/**/
			
				/*
				// output class
				try {
					File f = new File("ModifiedClass.class");
					FileOutputStream fos = new FileOutputStream(f);
					fos.write(instrumentedClass);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				/**/
			}
			
    	} catch(Throwable e) {
    		e.printStackTrace();
    	}
    	
        return instrumentedClass;
    }

}
