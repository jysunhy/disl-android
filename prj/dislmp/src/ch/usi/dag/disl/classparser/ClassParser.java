package ch.usi.dag.disl.classparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import ch.usi.dag.disl.annotation.ArgumentProcessor;
import ch.usi.dag.disl.exception.GuardException;
import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.exception.ParserException;
import ch.usi.dag.disl.exception.ProcessorParserException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.ScopeParserException;
import ch.usi.dag.disl.exception.SnippetParserException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.localvar.LocalVars;
import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.snippet.Snippet;

public class ClassParser {

	SnippetParser snippetParser = new SnippetParser();
	ProcessorParser processorParser = new ProcessorParser();
	
	public void parse(InputStream is) throws ParserException,
			SnippetParserException, ReflectionException, ScopeParserException,
			StaticContextGenException, ProcessorParserException,
			MarkerException, GuardException {

		// prepare class node
		ClassReader cr;
		try {
			cr = new ClassReader(is);
		} catch (IOException e) {
			// rethrow exception as DiSL exception
			throw new ParserException(e);
		}
		ClassNode classNode = new ClassNode();

		cr.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

		// decide according to processor annotation if it is a snippet or
		// processor
		
		// *** snippet ***
		if (classNode.invisibleAnnotations == null) {
			snippetParser.parse(classNode);
			return;
		}

		// *** processor ***
		
		// check for one annotation
		if (classNode.invisibleAnnotations.size() > 1) {
			throw new ParserException("Class " + classNode.name
					+ " may have only one anotation");
		}

		AnnotationNode annotation = 
			(AnnotationNode) classNode.invisibleAnnotations.get(0);

		Type annotationType = Type.getType(annotation.desc);

		// check for processor annotation
		if (! annotationType.equals(Type.getType(ArgumentProcessor.class))) {
			throw new ParserException("Class " + classNode.name
					+ " may have only ArgumentProcessor anotation");
		}
		
		processorParser.parse(classNode);
	}
	
	public LocalVars getAllLocalVars() {
		
		LocalVars merged = new LocalVars();
		
		// merge local variables from snippets and processors 
		merged.putAll(snippetParser.getAllLocalVars());
		merged.putAll(processorParser.getAllLocalVars());
		
		return merged;
	}
	
	public List<Snippet> getSnippets() {
		return snippetParser.getSnippets();
	}
	
	public Map<Type, Proc> getProcessors() {
		return processorParser.getProcessors();
	}
}
