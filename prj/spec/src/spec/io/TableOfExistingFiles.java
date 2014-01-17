/*
 * @(#)TableOfExistingFiles.java	1.5 06/17/98
 *
 * Copyright (c) 1998 Standard Performance Evaluation Corporation (SPEC)
 *               All rights reserved.
 * Copyright (c) 1997,1998 Sun Microsystems, Inc. All rights reserved.
 *
 * This source code is provided as is, without any express or implied warranty.
 */

package spec.io;

/**
 This class maintains a list of the files that should not be loaded across
 the net. Any other class or data file required by the SpecJVMClient are
 loaded across the net.
 */
public class TableOfExistingFiles extends java.util.Hashtable{

/**
 Constructor. 
 */
public TableOfExistingFiles(){
super();

String o = "Java programmers make better lovers.";
super.put("lib/java/util" , o);
super.put("lib/java/io" , o);
super.put("lib/java/lang/Object.class" , o);
super.put("lib/java/lang/Exception.class" , o);
super.put("lib/java/lang/Integer.class" , o);
super.put("lib/java/lang/NumberFormatException.class" , o);
super.put("lib/java/lang/Throwable.class" , o);
super.put("lib/java/lang/Class.class" , o);
super.put("lib/java/lang/IllegalAccessException.class" , o);
super.put("lib/java/lang/StringBuffer.class" , o);
super.put("lib/java/lang/ClassNotFoundException.class" , o);
super.put("lib/java/lang/IllegalArgumentException.class" , o);
super.put("lib/java/lang/Number.class" , o);
super.put("lib/java/lang/InterruptedException.class" , o);
super.put("lib/java/lang/String.class" , o);
super.put("lib/java/lang/RuntimeException.class" , o);
super.put("lib/java/lang/InternalError.class" , o);
super.put("lib/java/lang/Long.class" , o);
super.put("lib/java/lang/Character.class" , o);
super.put("lib/java/lang/CloneNotSupportedException.class" , o);
super.put("lib/java/lang/InstantiationException.class" , o);
super.put("lib/java/lang/VirtualMachineError.class" , o);
super.put("lib/java/lang/Double.class" , o);
super.put("lib/java/lang/Error.class" , o);
super.put("lib/java/lang/NullPointerException.class" , o);
super.put("lib/java/lang/Cloneable.class" , o);
super.put("lib/java/lang/System.class" , o);
super.put("lib/java/lang/ClassLoader.class" , o);
super.put("lib/java/lang/Math.class" , o);
super.put("lib/java/lang/Float.class" , o);
super.put("lib/java/lang/Runtime.class" , o);
super.put("lib/java/lang/StringIndexOutOfBoundsException.class" , o);
super.put("lib/java/lang/IndexOutOfBoundsException.class" , o);
super.put("lib/java/lang/SecurityException.class" , o);
super.put("lib/java/lang/LinkageError.class" , o);
super.put("lib/java/lang/Runnable.class" , o);
super.put("lib/java/lang/Process.class" , o);
super.put("lib/java/lang/SecurityManager.class" , o);
super.put("lib/java/lang/Thread.class" , o);
super.put("lib/java/lang/UnsatisfiedLinkError.class" , o);
super.put("lib/java/lang/IncompatibleClassChangeError.class" , o);
super.put("lib/java/lang/NoSuchMethodError.class" , o);
super.put("lib/java/lang/IllegalThreadStateException.class" , o);
super.put("lib/java/lang/ThreadGroup.class" , o);
super.put("lib/java/lang/ThreadDeath.class" , o);
super.put("lib/java/lang/ArrayIndexOutOfBoundsException.class" , o);
super.put("lib/java/lang/Boolean.class" , o);
super.put("lib/java/lang/Compiler.class" , o);
super.put("lib/java/lang/NoSuchMethodException.class" , o);
super.put("lib/java/lang/ArithmeticException.class" , o);
super.put("lib/java/lang/ArrayStoreException.class" , o);
super.put("lib/java/lang/ClassCastException.class" , o);
super.put("lib/java/lang/NegativeArraySizeException.class" , o);
super.put("lib/java/lang/IllegalMonitorStateException.class" , o);
super.put("lib/java/lang/ClassCircularityError.class" , o);
super.put("lib/java/lang/ClassFormatError.class" , o);
super.put("lib/java/lang/AbstractMethodError.class" , o);
super.put("lib/java/lang/IllegalAccessError.class" , o);
super.put("lib/java/lang/InstantiationError.class" , o);
super.put("lib/java/lang/NoSuchFieldError.class" , o);
super.put("lib/java/lang/NoClassDefFoundError.class" , o);
super.put("lib/java/lang/VerifyError.class" , o);
super.put("lib/java/lang/OutOfMemoryError.class" , o);
super.put("lib/java/lang/StackOverflowError.class" , o);
super.put("lib/java/lang/UnknownError.class" , o);
super.put("lib/java/lang/Win32Process.class" , o);
super.put("lib/java/io/FilterOutputStream.class" , o);
super.put("lib/java/io/OutputStream.class" , o);
super.put("lib/java/io/IOException.class" , o);
super.put("lib/java/io/PrintStream.class" , o);
super.put("lib/java/io/FileInputStream.class" , o);
super.put("lib/java/io/InterruptedIOException.class" , o);
super.put("lib/java/io/File.class" , o);
super.put("lib/java/io/InputStream.class" , o);
super.put("lib/java/io/BufferedInputStream.class" , o);
super.put("lib/java/io/FileOutputStream.class" , o);
super.put("lib/java/io/FileNotFoundException.class" , o);
super.put("lib/java/io/BufferedOutputStream.class" , o);
super.put("lib/java/io/FileDescriptor.class" , o);
super.put("lib/java/io/FilenameFilter.class" , o);
super.put("lib/java/io/FilterInputStream.class" , o);
super.put("lib/java/io/PipedInputStream.class" , o);
super.put("lib/java/io/PipedOutputStream.class" , o);
super.put("lib/java/io/EOFException.class" , o);
super.put("lib/java/io/UTFDataFormatException.class" , o);
super.put("lib/java/io/DataInput.class" , o);
super.put("lib/java/io/DataOutput.class" , o);
super.put("lib/java/io/DataInputStream.class" , o);
super.put("lib/java/io/PushbackInputStream.class" , o);
super.put("lib/java/io/ByteArrayInputStream.class" , o);
super.put("lib/java/io/SequenceInputStream.class" , o);
super.put("lib/java/io/StringBufferInputStream.class" , o);
super.put("lib/java/io/LineNumberInputStream.class" , o);
super.put("lib/java/io/DataOutputStream.class" , o);
super.put("lib/java/io/ByteArrayOutputStream.class" , o);
super.put("lib/java/io/RandomAccessFile.class" , o);
super.put("lib/java/io/StreamTokenizer.class" , o);
super.put("lib/java/util/Hashtable.class" , o);
super.put("lib/java/util/Enumeration.class" , o);
super.put("lib/java/util/HashtableEnumerator.class" , o);
super.put("lib/java/util/Properties.class" , o);
super.put("lib/java/util/HashtableEntry.class" , o);
super.put("lib/java/util/Dictionary.class" , o);
super.put("lib/java/util/Date.class" , o);
super.put("lib/java/util/NoSuchElementException.class" , o);
super.put("lib/java/util/StringTokenizer.class" , o);
super.put("lib/java/util/Random.class" , o);
super.put("lib/java/util/VectorEnumerator.class" , o);
super.put("lib/java/util/Vector.class" , o);
super.put("lib/java/util/BitSet.class" , o);
super.put("lib/java/util/EmptyStackException.class" , o);
super.put("lib/java/util/Observable.class" , o);
super.put("lib/java/util/Observer.class" , o);
super.put("lib/java/util/ObserverList.class" , o);
super.put("lib/java/util/Stack.class" , o);
super.put("lib/java/awt/Toolkit.class" , o);
super.put("lib/java/awt/peer/WindowPeer.class" , o);
super.put("lib/java/awt/peer/TextFieldPeer.class" , o);
super.put("lib/java/awt/peer/ContainerPeer.class" , o);
super.put("lib/java/awt/peer/PanelPeer.class" , o);
super.put("lib/java/awt/peer/CanvasPeer.class" , o);
super.put("lib/java/awt/peer/FramePeer.class" , o);
super.put("lib/java/awt/peer/ChoicePeer.class" , o);
super.put("lib/java/awt/peer/CheckboxMenuItemPeer.class" , o);
super.put("lib/java/awt/peer/TextAreaPeer.class" , o);
super.put("lib/java/awt/peer/FileDialogPeer.class" , o);
super.put("lib/java/awt/peer/TextComponentPeer.class" , o);
super.put("lib/java/awt/peer/ScrollbarPeer.class" , o);
super.put("lib/java/awt/peer/ButtonPeer.class" , o);
super.put("lib/java/awt/peer/ComponentPeer.class" , o);
super.put("lib/java/awt/peer/MenuComponentPeer.class" , o);
super.put("lib/java/awt/peer/MenuItemPeer.class" , o);
super.put("lib/java/awt/peer/CheckboxPeer.class" , o);
super.put("lib/java/awt/peer/MenuPeer.class" , o);
super.put("lib/java/awt/peer/ListPeer.class" , o);
super.put("lib/java/awt/peer/MenuBarPeer.class" , o);
super.put("lib/java/awt/peer/LabelPeer.class" , o);
super.put("lib/java/awt/peer/DialogPeer.class" , o);
super.put("lib/java/awt/Image.class" , o);
super.put("lib/java/awt/MenuItem.class" , o);
super.put("lib/java/awt/MenuComponent.class" , o);
super.put("lib/java/awt/image/ImageProducer.class" , o);
super.put("lib/java/awt/image/ColorModel.class" , o);
super.put("lib/java/awt/image/DirectColorModel.class" , o);
super.put("lib/java/awt/image/ImageConsumer.class" , o);
super.put("lib/java/awt/image/ImageObserver.class" , o);
super.put("lib/java/awt/image/CropImageFilter.class" , o);
super.put("lib/java/awt/image/ImageFilter.class" , o);
super.put("lib/java/awt/image/FilteredImageSource.class" , o);
super.put("lib/java/awt/image/IndexColorModel.class" , o);
super.put("lib/java/awt/image/MemoryImageSource.class" , o);
super.put("lib/java/awt/image/PixelGrabber.class" , o);
super.put("lib/java/awt/image/RGBImageFilter.class" , o);
super.put("lib/java/awt/FontMetrics.class" , o);
super.put("lib/java/awt/Checkbox.class" , o);
super.put("lib/java/awt/CheckboxGroup.class" , o);
super.put("lib/java/awt/MenuContainer.class" , o);
super.put("lib/java/awt/Menu.class" , o);
super.put("lib/java/awt/Insets.class" , o);
super.put("lib/java/awt/MenuBar.class" , o);
super.put("lib/java/awt/List.class" , o);
super.put("lib/java/awt/Label.class" , o);
super.put("lib/java/awt/Component.class" , o);
super.put("lib/java/awt/TextField.class" , o);
super.put("lib/java/awt/TextComponent.class" , o);
super.put("lib/java/awt/Dialog.class" , o);
super.put("lib/java/awt/Font.class" , o);
super.put("lib/java/awt/Window.class" , o);
super.put("lib/java/awt/FocusManager.class" , o);
super.put("lib/java/awt/Panel.class" , o);
super.put("lib/java/awt/Container.class" , o);
super.put("lib/java/awt/Graphics.class" , o);
super.put("lib/java/awt/CheckboxMenuItem.class" , o);
super.put("lib/java/awt/Canvas.class" , o);
super.put("lib/java/awt/Frame.class" , o);
super.put("lib/java/awt/Choice.class" , o);
super.put("lib/java/awt/Event.class" , o);
super.put("lib/java/awt/TextArea.class" , o);
super.put("lib/java/awt/AWTError.class" , o);
super.put("lib/java/awt/Polygon.class" , o);
super.put("lib/java/awt/FlowLayout.class" , o);
super.put("lib/java/awt/Point.class" , o);
super.put("lib/java/awt/FileDialog.class" , o);
super.put("lib/java/awt/Scrollbar.class" , o);
super.put("lib/java/awt/Dimension.class" , o);
super.put("lib/java/awt/Color.class" , o);
super.put("lib/java/awt/Button.class" , o);
super.put("lib/java/awt/LayoutManager.class" , o);
super.put("lib/java/awt/Rectangle.class" , o);
super.put("lib/java/awt/BorderLayout.class" , o);
super.put("lib/java/awt/GridLayout.class" , o);
super.put("lib/java/awt/GridBagConstraints.class" , o);
super.put("lib/java/awt/GridBagLayout.class" , o);
super.put("lib/java/awt/GridBagLayoutInfo.class" , o);
super.put("lib/java/awt/CardLayout.class" , o);
super.put("lib/java/awt/MediaTracker.class" , o);
super.put("lib/java/awt/MediaEntry.class" , o);
super.put("lib/java/awt/ImageMediaEntry.class" , o);
super.put("lib/java/awt/AWTException.class" , o);
super.put("lib/java/net/URL.class" , o);
super.put("lib/java/net/URLStreamHandlerFactory.class" , o);
super.put("lib/java/net/InetAddress.class" , o);
super.put("lib/java/net/UnknownContentHandler.class" , o);
super.put("lib/java/net/UnknownHostException.class" , o);
super.put("lib/java/net/URLStreamHandler.class" , o);
super.put("lib/java/net/URLConnection.class" , o);
super.put("lib/java/net/MalformedURLException.class" , o);
super.put("lib/java/net/ContentHandlerFactory.class" , o);
super.put("lib/java/net/ContentHandler.class" , o);
super.put("lib/java/net/UnknownServiceException.class" , o);
super.put("lib/java/net/ServerSocket.class" , o);
super.put("lib/java/net/PlainSocketImpl.class" , o);
super.put("lib/java/net/SocketImpl.class" , o);
super.put("lib/java/net/ProtocolException.class" , o);
super.put("lib/java/net/SocketException.class" , o);
super.put("lib/java/net/SocketInputStream.class" , o);
super.put("lib/java/net/Socket.class" , o);
super.put("lib/java/net/SocketImplFactory.class" , o);
super.put("lib/java/net/SocketOutputStream.class" , o);
super.put("lib/java/net/DatagramPacket.class" , o);
super.put("lib/java/net/DatagramSocket.class" , o);
super.put("lib/java/net/URLEncoder.class" , o);
super.put("lib/java/applet/Applet.class" , o);
super.put("lib/java/applet/AppletContext.class" , o);
super.put("lib/java/applet/AudioClip.class" , o);
super.put("lib/java/applet/AppletStub.class" , o);
super.put("lib/sun/tools/debug/BreakpointQueue.class" , o);
super.put("lib/sun/tools/debug/DebuggerCallback.class" , o);
super.put("lib/sun/tools/debug/RemoteThread.class" , o);
super.put("lib/sun/tools/debug/StackFrame.class" , o);
super.put("lib/sun/tools/debug/RemoteAgent.class" , o);
super.put("lib/sun/tools/debug/AgentConstants.class" , o);
super.put("lib/sun/tools/debug/AgentIn.class" , o);
super.put("lib/sun/tools/debug/RemoteObject.class" , o);
super.put("lib/sun/tools/debug/RemoteStackVariable.class" , o);
super.put("lib/sun/tools/debug/RemoteValue.class" , o);
super.put("lib/sun/tools/debug/RemoteClass.class" , o);
super.put("lib/sun/tools/debug/Agent.class" , o);
super.put("lib/sun/tools/debug/RemoteBoolean.class" , o);
super.put("lib/sun/tools/debug/RemoteChar.class" , o);
super.put("lib/sun/tools/debug/RemoteString.class" , o);
super.put("lib/sun/tools/debug/NoSessionException.class" , o);
super.put("lib/sun/tools/debug/Field.class" , o);
super.put("lib/sun/tools/debug/NoSuchLineNumberException.class" , o);
super.put("lib/sun/tools/debug/RemoteShort.class" , o);
super.put("lib/sun/tools/debug/RemoteThreadGroup.class" , o);
super.put("lib/sun/tools/debug/RemoteInt.class" , o);
super.put("lib/sun/tools/debug/ResponseStream.class" , o);
super.put("lib/sun/tools/debug/RemoteDouble.class" , o);
super.put("lib/sun/tools/debug/LocalVariable.class" , o);
super.put("lib/sun/tools/debug/BreakpointSet.class" , o);
super.put("lib/sun/tools/debug/RemoteStackFrame.class" , o);
super.put("lib/sun/tools/debug/MainThread.class" , o);
super.put("lib/sun/tools/debug/BreakpointHandler.class" , o);
super.put("lib/sun/tools/debug/AgentOutputStream.class" , o);
super.put("lib/sun/tools/debug/RemoteLong.class" , o);
super.put("lib/sun/tools/debug/RemoteFloat.class" , o);
super.put("lib/sun/tools/debug/RemoteArray.class" , o);
super.put("lib/sun/tools/debug/InvalidPCException.class" , o);
super.put("lib/sun/tools/debug/LineNumber.class" , o);
super.put("lib/sun/tools/debug/RemoteField.class" , o);
super.put("lib/sun/tools/debug/NoSuchFieldException.class" , o);
super.put("lib/sun/tools/debug/RemoteByte.class" , o);
super.put("lib/sun/tools/debug/EmptyApp.class" , o);
super.put("lib/sun/tools/debug/RemoteDebugger.class" , o);
super.put("lib/sun/tools/java/RuntimeConstants.class" , o);
super.put("lib/sun/tools/java/Constants.class" , o);
super.put("lib/sun/tools/java/Environment.class" , o);
super.put("lib/sun/tools/java/ClassPath.class" , o);
super.put("lib/sun/tools/java/ClassDeclaration.class" , o);
super.put("lib/sun/tools/java/FieldDefinition.class" , o);
super.put("lib/sun/tools/java/Type.class" , o);
super.put("lib/sun/tools/java/ClassNotFound.class" , o);
super.put("lib/sun/tools/java/ClassType.class" , o);
super.put("lib/sun/tools/java/ClassDefinition.class" , o);
super.put("lib/sun/tools/java/Parser.class" , o);
super.put("lib/sun/tools/java/ClassPathEntry.class" , o);
super.put("lib/sun/tools/java/CompilerError.class" , o);
super.put("lib/sun/tools/java/Identifier.class" , o);
super.put("lib/sun/tools/java/Package.class" , o);
super.put("lib/sun/tools/java/ClassFile.class" , o);
super.put("lib/sun/tools/java/Imports.class" , o);
super.put("lib/sun/tools/java/ArrayType.class" , o);
super.put("lib/sun/tools/java/AmbiguousField.class" , o);
super.put("lib/sun/tools/java/MethodType.class" , o);
super.put("lib/sun/tools/java/Scanner.class" , o);
super.put("lib/sun/tools/java/SyntaxError.class" , o);
super.put("lib/sun/tools/java/BinaryClass.class" , o);
super.put("lib/sun/tools/java/BinaryField.class" , o);
super.put("lib/sun/tools/java/AmbiguousClass.class" , o);
super.put("lib/sun/tools/java/BinaryConstantPool.class" , o);
super.put("lib/sun/tools/java/ScannerInputStream.class" , o);
super.put("lib/sun/tools/java/BinaryAttribute.class" , o);
super.put("lib/sun/tools/java/BinaryCode.class" , o);
super.put("lib/sun/tools/java/BinaryExceptionHandler.class" , o);
super.put("lib/sun/tools/javac/Main.class" , o);
super.put("lib/sun/tools/javac/SourceClass.class" , o);
super.put("lib/sun/tools/javac/CompilerField.class" , o);
super.put("lib/sun/tools/javac/SourceField.class" , o);
super.put("lib/sun/tools/javac/BatchEnvironment.class" , o);
super.put("lib/sun/tools/javac/ErrorConsumer.class" , o);
super.put("lib/sun/tools/javac/ErrorMessage.class" , o);
super.put("lib/sun/tools/javac/BatchParser.class" , o);
super.put("lib/sun/tools/zip/ZipFile.class" , o);
super.put("lib/sun/tools/zip/ZipEntry.class" , o);
super.put("lib/sun/tools/zip/ZipFileInputStream.class" , o);
super.put("lib/sun/tools/zip/ZipConstants.class" , o);
super.put("lib/sun/tools/zip/ZipFormatException.class" , o);
super.put("lib/sun/tools/zip/ZipReaderInputStream.class" , o);
super.put("lib/sun/tools/zip/ZipReader.class" , o);
super.put("lib/sun/tools/tree/ConstantExpression.class" , o);
super.put("lib/sun/tools/tree/LocalField.class" , o);
super.put("lib/sun/tools/tree/Expression.class" , o);
super.put("lib/sun/tools/tree/IncDecExpression.class" , o);
super.put("lib/sun/tools/tree/SuperExpression.class" , o);
super.put("lib/sun/tools/tree/NaryExpression.class" , o);
super.put("lib/sun/tools/tree/StringExpression.class" , o);
super.put("lib/sun/tools/tree/UnaryExpression.class" , o);
super.put("lib/sun/tools/tree/Context.class" , o);
super.put("lib/sun/tools/tree/ExpressionStatement.class" , o);
super.put("lib/sun/tools/tree/ConditionVars.class" , o);
super.put("lib/sun/tools/tree/Node.class" , o);
super.put("lib/sun/tools/tree/CharExpression.class" , o);
super.put("lib/sun/tools/tree/CaseStatement.class" , o);
super.put("lib/sun/tools/tree/LessExpression.class" , o);
super.put("lib/sun/tools/tree/IntegerExpression.class" , o);
super.put("lib/sun/tools/tree/SubtractExpression.class" , o);
super.put("lib/sun/tools/tree/ArrayAccessExpression.class" , o);
super.put("lib/sun/tools/tree/TryStatement.class" , o);
super.put("lib/sun/tools/tree/BinaryEqualityExpression.class" , o);
super.put("lib/sun/tools/tree/Statement.class" , o);
super.put("lib/sun/tools/tree/AssignSubtractExpression.class" , o);
super.put("lib/sun/tools/tree/FinallyStatement.class" , o);
super.put("lib/sun/tools/tree/ForStatement.class" , o);
super.put("lib/sun/tools/tree/DivRemExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryExpression.class" , o);
super.put("lib/sun/tools/tree/ShiftRightExpression.class" , o);
super.put("lib/sun/tools/tree/AssignMultiplyExpression.class" , o);
super.put("lib/sun/tools/tree/BooleanExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryArithmeticExpression.class" , o);
super.put("lib/sun/tools/tree/ThrowStatement.class" , o);
super.put("lib/sun/tools/tree/AssignDivideExpression.class" , o);
super.put("lib/sun/tools/tree/AssignShiftLeftExpression.class" , o);
super.put("lib/sun/tools/tree/NewArrayExpression.class" , o);
super.put("lib/sun/tools/tree/AndExpression.class" , o);
super.put("lib/sun/tools/tree/AssignBitOrExpression.class" , o);
super.put("lib/sun/tools/tree/BreakStatement.class" , o);
super.put("lib/sun/tools/tree/SynchronizedStatement.class" , o);
super.put("lib/sun/tools/tree/PreDecExpression.class" , o);
super.put("lib/sun/tools/tree/CompoundStatement.class" , o);
super.put("lib/sun/tools/tree/DoubleExpression.class" , o);
super.put("lib/sun/tools/tree/ConvertExpression.class" , o);
super.put("lib/sun/tools/tree/NullExpression.class" , o);
super.put("lib/sun/tools/tree/LessOrEqualExpression.class" , o);
super.put("lib/sun/tools/tree/IdentifierExpression.class" , o);
super.put("lib/sun/tools/tree/ReturnStatement.class" , o);
super.put("lib/sun/tools/tree/BitNotExpression.class" , o);
super.put("lib/sun/tools/tree/LongExpression.class" , o);
super.put("lib/sun/tools/tree/VarDeclarationStatement.class" , o);
super.put("lib/sun/tools/tree/MethodExpression.class" , o);
super.put("lib/sun/tools/tree/ThisExpression.class" , o);
super.put("lib/sun/tools/tree/BitOrExpression.class" , o);
super.put("lib/sun/tools/tree/PositiveExpression.class" , o);
super.put("lib/sun/tools/tree/IfStatement.class" , o);
super.put("lib/sun/tools/tree/FloatExpression.class" , o);
super.put("lib/sun/tools/tree/NotEqualExpression.class" , o);
super.put("lib/sun/tools/tree/InstanceOfExpression.class" , o);
super.put("lib/sun/tools/tree/NotExpression.class" , o);
super.put("lib/sun/tools/tree/BitAndExpression.class" , o);
super.put("lib/sun/tools/tree/DivideExpression.class" , o);
super.put("lib/sun/tools/tree/ShortExpression.class" , o);
super.put("lib/sun/tools/tree/RemainderExpression.class" , o);
super.put("lib/sun/tools/tree/NewInstanceExpression.class" , o);
super.put("lib/sun/tools/tree/SwitchStatement.class" , o);
super.put("lib/sun/tools/tree/AddExpression.class" , o);
super.put("lib/sun/tools/tree/AssignOpExpression.class" , o);
super.put("lib/sun/tools/tree/EqualExpression.class" , o);
super.put("lib/sun/tools/tree/PostIncExpression.class" , o);
super.put("lib/sun/tools/tree/GreaterExpression.class" , o);
super.put("lib/sun/tools/tree/PostDecExpression.class" , o);
super.put("lib/sun/tools/tree/AssignExpression.class" , o);
super.put("lib/sun/tools/tree/WhileStatement.class" , o);
super.put("lib/sun/tools/tree/ContinueStatement.class" , o);
super.put("lib/sun/tools/tree/ConditionalExpression.class" , o);
super.put("lib/sun/tools/tree/AssignAddExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryBitExpression.class" , o);
super.put("lib/sun/tools/tree/CastExpression.class" , o);
super.put("lib/sun/tools/tree/AssignBitXorExpression.class" , o);
super.put("lib/sun/tools/tree/ArrayExpression.class" , o);
super.put("lib/sun/tools/tree/InlineMethodExpression.class" , o);
super.put("lib/sun/tools/tree/InlineNewInstanceExpression.class" , o);
super.put("lib/sun/tools/tree/CodeContext.class" , o);
super.put("lib/sun/tools/tree/AssignShiftRightExpression.class" , o);
super.put("lib/sun/tools/tree/UnsignedShiftRightExpression.class" , o);
super.put("lib/sun/tools/tree/AssignBitAndExpression.class" , o);
super.put("lib/sun/tools/tree/ShiftLeftExpression.class" , o);
super.put("lib/sun/tools/tree/CatchStatement.class" , o);
super.put("lib/sun/tools/tree/IntExpression.class" , o);
super.put("lib/sun/tools/tree/TypeExpression.class" , o);
super.put("lib/sun/tools/tree/CommaExpression.class" , o);
super.put("lib/sun/tools/tree/AssignUnsignedShiftRightExpression.class" , o);
super.put("lib/sun/tools/tree/ExprExpression.class" , o);
super.put("lib/sun/tools/tree/AssignRemainderExpression.class" , o);
super.put("lib/sun/tools/tree/ByteExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryAssignExpression.class" , o);
super.put("lib/sun/tools/tree/DoStatement.class" , o);
super.put("lib/sun/tools/tree/DeclarationStatement.class" , o);
super.put("lib/sun/tools/tree/MultiplyExpression.class" , o);
super.put("lib/sun/tools/tree/InlineReturnStatement.class" , o);
super.put("lib/sun/tools/tree/BitXorExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryCompareExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryShiftExpression.class" , o);
super.put("lib/sun/tools/tree/CheckContext.class" , o);
super.put("lib/sun/tools/tree/PreIncExpression.class" , o);
super.put("lib/sun/tools/tree/GreaterOrEqualExpression.class" , o);
super.put("lib/sun/tools/tree/FieldExpression.class" , o);
super.put("lib/sun/tools/tree/OrExpression.class" , o);
super.put("lib/sun/tools/tree/BinaryLogicalExpression.class" , o);
super.put("lib/sun/tools/tree/NegativeExpression.class" , o);
super.put("lib/sun/tools/tree/LengthExpression.class" , o);
super.put("lib/sun/tools/asm/Assembler.class" , o);
super.put("lib/sun/tools/asm/Instruction.class" , o);
super.put("lib/sun/tools/asm/LocalVariable.class" , o);
super.put("lib/sun/tools/asm/ArrayData.class" , o);
super.put("lib/sun/tools/asm/LocalVariableTable.class" , o);
super.put("lib/sun/tools/asm/SwitchDataEnumeration.class" , o);
super.put("lib/sun/tools/asm/ConstantPool.class" , o);
super.put("lib/sun/tools/asm/ConstantPoolData.class" , o);
super.put("lib/sun/tools/asm/NameAndTypeConstantData.class" , o);
super.put("lib/sun/tools/asm/NumberConstantData.class" , o);
super.put("lib/sun/tools/asm/FieldConstantData.class" , o);
super.put("lib/sun/tools/asm/TryData.class" , o);
super.put("lib/sun/tools/asm/Label.class" , o);
super.put("lib/sun/tools/asm/SwitchData.class" , o);
super.put("lib/sun/tools/asm/CatchData.class" , o);
super.put("lib/sun/tools/asm/StringExpressionConstantData.class" , o);
super.put("lib/sun/tools/asm/NameAndTypeData.class" , o);
super.put("lib/sun/tools/asm/StringConstantData.class" , o);
super.put("lib/sun/tools/asm/ClassConstantData.class" , o);
super.put("lib/sun/tools/ttydebug/TTY.class" , o);
super.put("lib/sun/tools/javadoc/Main.class" , o);
super.put("lib/sun/tools/javadoc/DocumentationGenerator.class" , o);
super.put("lib/sun/tools/javadoc/HTMLDocumentationGenerator.class" , o);
super.put("lib/sun/tools/javadoc/MIFDocumentationGenerator.class" , o);
super.put("lib/sun/tools/javadoc/MIFPrintStream.class" , o);
super.put("lib/sun/net/MulticastSocket.class" , o);
super.put("lib/sun/net/URLCanonicalizer.class" , o);
super.put("lib/sun/net/NetworkClient.class" , o);
super.put("lib/sun/net/NetworkServer.class" , o);
super.put("lib/sun/net/ProgressData.class" , o);
super.put("lib/sun/net/ProgressEntry.class" , o);
super.put("lib/sun/net/TelnetInputStream.class" , o);
super.put("lib/sun/net/TelnetProtocolException.class" , o);
super.put("lib/sun/net/TelnetOutputStream.class" , o);
super.put("lib/sun/net/TransferProtocolClient.class" , o);
super.put("lib/sun/net/ftp/FtpInputStream.class" , o);
super.put("lib/sun/net/ftp/FtpClient.class" , o);
super.put("lib/sun/net/ftp/FtpLoginException.class" , o);
super.put("lib/sun/net/ftp/FtpProtocolException.class" , o);
super.put("lib/sun/net/ftp/IftpClient.class" , o);
super.put("lib/sun/net/nntp/NewsgroupInfo.class" , o);
super.put("lib/sun/net/nntp/NntpClient.class" , o);
super.put("lib/sun/net/nntp/UnknownNewsgroupException.class" , o);
super.put("lib/sun/net/nntp/NntpProtocolException.class" , o);
super.put("lib/sun/net/nntp/NntpInputStream.class" , o);
super.put("lib/sun/net/smtp/SmtpPrintStream.class" , o);
super.put("lib/sun/net/smtp/SmtpClient.class" , o);
super.put("lib/sun/net/smtp/SmtpProtocolException.class" , o);
super.put("lib/sun/net/www/auth/Authenticator.class" , o);
super.put("lib/sun/net/www/auth/basic.class" , o);
super.put("lib/sun/net/www/content/text/Generic.class" , o);
super.put("lib/sun/net/www/content/text/plain.class" , o);
super.put("lib/sun/net/www/content/image/gif.class" , o);
super.put("lib/sun/net/www/content/image/jpeg.class" , o);
super.put("lib/sun/net/www/content/image/x_xbitmap.class" , o);
super.put("lib/sun/net/www/content/image/x_xpixmap.class" , o);
super.put("lib/sun/net/www/FormatException.class" , o);
super.put("lib/sun/net/www/MessageHeader.class" , o);
super.put("lib/sun/net/www/MeteredStream.class" , o);
super.put("lib/sun/net/www/ProgressReport.class" , o);
super.put("lib/sun/net/www/MimeEntry.class" , o);
super.put("lib/sun/net/www/MimeLauncher.class" , o);
super.put("lib/sun/net/www/MimeTable.class" , o);
super.put("lib/sun/net/www/URLConnection.class" , o);
super.put("lib/sun/net/www/UnknownContentException.class" , o);
super.put("lib/sun/net/www/UnknownContentHandler.class" , o);
super.put("lib/sun/net/www/protocol/file/Handler.class" , o);
super.put("lib/sun/net/www/protocol/file/FileURLConnection.class" , o);
super.put("lib/sun/net/www/protocol/http/Handler.class" , o);
super.put("lib/sun/net/www/protocol/http/HttpURLConnection.class" , o);
super.put("lib/sun/net/www/protocol/http/HttpPostBufferStream.class" , o);
super.put("lib/sun/net/www/protocol/doc/Handler.class" , o);
super.put("lib/sun/net/www/protocol/verbatim/Handler.class" , o);
super.put("lib/sun/net/www/protocol/verbatim/VerbatimConnection.class" , o);
super.put("lib/sun/net/www/protocol/gopher/GopherClient.class" , o);
super.put("lib/sun/net/www/protocol/gopher/GopherInputStream.class" , o);
super.put("lib/sun/net/www/http/UnauthorizedHttpRequestException.class" , o);
super.put("lib/sun/net/www/http/HttpClient.class" , o);
super.put("lib/sun/net/www/http/AuthenticationInfo.class" , o);
super.put("lib/sun/awt/HorizBagLayout.class" , o);
super.put("lib/sun/awt/VerticalBagLayout.class" , o);
super.put("lib/sun/awt/VariableGridLayout.class" , o);
super.put("lib/sun/awt/FocusingTextField.class" , o);
super.put("lib/sun/awt/win32/MToolkit.class" , o);
super.put("lib/sun/awt/win32/MMenuBarPeer.class" , o);
super.put("lib/sun/awt/win32/MButtonPeer.class" , o);
super.put("lib/sun/awt/win32/Win32Image.class" , o);
super.put("lib/sun/awt/win32/MScrollbarPeer.class" , o);
super.put("lib/sun/awt/win32/MDialogPeer.class" , o);
super.put("lib/sun/awt/win32/MCheckboxMenuItemPeer.class" , o);
super.put("lib/sun/awt/win32/Win32Graphics.class" , o);
super.put("lib/sun/awt/win32/MListPeer.class" , o);
super.put("lib/sun/awt/win32/MWindowPeer.class" , o);
super.put("lib/sun/awt/win32/MMenuItemPeer.class" , o);
super.put("lib/sun/awt/win32/ModalThread.class" , o);
super.put("lib/sun/awt/win32/MCanvasPeer.class" , o);
super.put("lib/sun/awt/win32/MFileDialogPeer.class" , o);
super.put("lib/sun/awt/win32/MTextAreaPeer.class" , o);
super.put("lib/sun/awt/win32/MPanelPeer.class" , o);
super.put("lib/sun/awt/win32/MComponentPeer.class" , o);
super.put("lib/sun/awt/win32/MCheckboxPeer.class" , o);
super.put("lib/sun/awt/win32/MLabelPeer.class" , o);
super.put("lib/sun/awt/win32/Win32FontMetrics.class" , o);
super.put("lib/sun/awt/win32/MFramePeer.class" , o);
super.put("lib/sun/awt/win32/MMenuPeer.class" , o);
super.put("lib/sun/awt/win32/MChoicePeer.class" , o);
super.put("lib/sun/awt/win32/MTextFieldPeer.class" , o);
super.put("lib/sun/awt/win32/Win32PrintJob.class" , o);
super.put("lib/sun/awt/image/URLImageSource.class" , o);
super.put("lib/sun/awt/image/ImageWatched.class" , o);
super.put("lib/sun/awt/image/InputStreamImageSource.class" , o);
super.put("lib/sun/awt/image/ConsumerQueue.class" , o);
super.put("lib/sun/awt/image/ImageDecoder.class" , o);
super.put("lib/sun/awt/image/ImageRepresentation.class" , o);
super.put("lib/sun/awt/image/ImageInfoGrabber.class" , o);
super.put("lib/sun/awt/image/XbmImageDecoder.class" , o);
super.put("lib/sun/awt/image/GifImageDecoder.class" , o);
super.put("lib/sun/awt/image/ImageFetcher.class" , o);
super.put("lib/sun/awt/image/PixelStore.class" , o);
super.put("lib/sun/awt/image/JPEGImageDecoder.class" , o);
super.put("lib/sun/awt/image/PixelStore8.class" , o);
super.put("lib/sun/awt/image/ImageFetchable.class" , o);
super.put("lib/sun/awt/image/OffScreenImageSource.class" , o);
super.put("lib/sun/awt/image/PixelStore32.class" , o);
super.put("lib/sun/awt/image/ImageFormatException.class" , o);
super.put("lib/sun/awt/image/FileImageSource.class" , o);
super.put("lib/sun/awt/image/Image.class" , o);
super.put("lib/sun/awt/UpdateClient.class" , o);
super.put("lib/sun/awt/ScreenUpdaterEntry.class" , o);
super.put("lib/sun/awt/ScreenUpdater.class" , o);
super.put("lib/sun/misc/Ref.class" , o);
super.put("lib/sun/misc/MessageUtils.class" , o);
super.put("lib/sun/misc/Cache.class" , o);
super.put("lib/sun/misc/CacheEntry.class" , o);
super.put("lib/sun/misc/CacheEnumerator.class" , o);
super.put("lib/sun/misc/CEFormatException.class" , o);
super.put("lib/sun/misc/CEStreamExhausted.class" , o);
super.put("lib/sun/misc/CRC16.class" , o);
super.put("lib/sun/misc/CharacterDecoder.class" , o);
super.put("lib/sun/misc/BASE64Decoder.class" , o);
super.put("lib/sun/misc/UCDecoder.class" , o);
super.put("lib/sun/misc/UUDecoder.class" , o);
super.put("lib/sun/misc/CharacterEncoder.class" , o);
super.put("lib/sun/misc/BASE64Encoder.class" , o);
super.put("lib/sun/misc/HexDumpEncoder.class" , o);
super.put("lib/sun/misc/UCEncoder.class" , o);
super.put("lib/sun/misc/UUEncoder.class" , o);
super.put("lib/sun/misc/Timeable.class" , o);
super.put("lib/sun/misc/TimerTickThread.class" , o);
super.put("lib/sun/misc/Timer.class" , o);
super.put("lib/sun/misc/TimerThread.class" , o);
super.put("lib/sun/misc/ConditionLock.class" , o);
super.put("lib/sun/misc/Lock.class" , o);
super.put("lib/sun/audio/AudioDataStream.class" , o);
super.put("lib/sun/audio/AudioData.class" , o);
super.put("lib/sun/audio/AudioDevice.class" , o);
super.put("lib/sun/audio/AudioPlayer.class" , o);
super.put("lib/sun/audio/AudioStream.class" , o);
super.put("lib/sun/audio/NativeAudioStream.class" , o);
super.put("lib/sun/audio/InvalidAudioFormatException.class" , o);
super.put("lib/sun/audio/AudioTranslatorStream.class" , o);
super.put("lib/sun/audio/AudioStreamSequence.class" , o);
super.put("lib/sun/audio/ContinuousAudioDataStream.class" , o);
super.put("lib/sun/applet/StdAppletViewerFactory.class" , o);
super.put("lib/sun/applet/TextFrame.class" , o);
super.put("lib/sun/applet/AppletViewerFactory.class" , o);
super.put("lib/sun/applet/AppletViewer.class" , o);
super.put("lib/sun/applet/AppletCopyright.class" , o);
super.put("lib/sun/applet/AppletAudioClip.class" , o);
super.put("lib/sun/applet/AppletSecurity.class" , o);
super.put("lib/sun/applet/AppletThreadGroup.class" , o);
super.put("lib/sun/applet/AppletClassLoader.class" , o);
super.put("lib/sun/applet/AppletPanel.class" , o);
super.put("lib/sun/applet/AppletViewerPanel.class" , o);
super.put("lib/sun/applet/AppletProps.class" , o);
super.put("lib/sun/applet/AppletSecurityException.class" , o);
super.put("lib/sun/applet/AppletZipClassLoader.class" , o);
super.put("lib/spec/benchmarks/_202_jess/Activation.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Binding.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Context.java" , o);
super.put("lib/spec/benchmarks/_202_jess/ContextState.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Deffacts.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Deffunction.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Defglobal.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Defrule.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Deftemplate.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Fact.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Funcall.java" , o);
super.put("lib/spec/benchmarks/_202_jess/GlobalContext.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Jesp.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Jess.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Main.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Node.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Node1.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Node2.java" , o);
super.put("lib/spec/benchmarks/_202_jess/NodeNot2.java" , o);
super.put("lib/spec/benchmarks/_202_jess/NodeTerm.java" , o);
super.put("lib/spec/benchmarks/_202_jess/NullDisplay.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Pattern.java" , o);
super.put("lib/spec/benchmarks/_202_jess/RU.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Rete.java" , o);
super.put("lib/spec/benchmarks/_202_jess/ReteCompiler.java" , o);
super.put("lib/spec/benchmarks/_202_jess/ReteDisplay.java" , o);
super.put("lib/spec/benchmarks/_202_jess/ReteException.java" , o);
super.put("lib/spec/benchmarks/_202_jess/StringFunctions.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Successor.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Test1.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Test2.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Token.java" , o);
super.put("lib/spec/benchmarks/_202_jess/TokenVector.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Userfunction.java" , o);
super.put("lib/spec/benchmarks/_202_jess/Value.java" , o);
super.put("lib/spec/benchmarks/_202_jess/ValueVector.java" , o);

}

/**
 Checks whether the given file exists in the list of existing files
 */
public boolean exists(String filename){
 
//   System.out.println( "*** "+filename+" = "+containsKey(filename) );
  return containsKey(filename);

}
}
