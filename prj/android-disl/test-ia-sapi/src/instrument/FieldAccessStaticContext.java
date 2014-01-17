/*
 * Abstract Shadow???Heap Analysis
 * Copyright (C) 2012 Technische Universit??t Darmstadt
 * info@scalabench.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package instrument;

import org.objectweb.asm.tree.FieldInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class FieldAccessStaticContext extends MethodStaticContext {
    
    /**
     * The character used to separate the components of the field identifier.
     */
    public static final char SUBSEP = '\034';
    
    public String getOwner() {
        return ((FieldInsnNode) staticContextData.getRegionStart()).owner;
    }
    
    public String getFieldId() {
    	return getName() + SUBSEP + getDesc();
    }
    
    private String getName() {
        return ((FieldInsnNode) staticContextData.getRegionStart()).name;
    }
    
    private String getDesc() {
        return ((FieldInsnNode) staticContextData.getRegionStart()).desc;
    }
}
