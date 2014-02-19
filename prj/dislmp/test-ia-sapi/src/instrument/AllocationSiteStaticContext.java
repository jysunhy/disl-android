/*
 * Abstract Shadow‐Heap Analysis
 * Copyright (C) 2012 Technische Universität Darmstadt
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

import static org.objectweb.asm.tree.AbstractInsnNode.*;

import org.objectweb.asm.tree.AbstractInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class AllocationSiteStaticContext extends MethodStaticContext {
    
    /**
     * The character used to separate the components of the allocation site identifier.
     */
    public static final char SUBSEP = '\034';
    
    public static final int OUT_OF_LINE_INDEX = -1;
    
    public String getAllocationSite() {
        int index = -1; // zero-based indices
        
        for (AbstractInsnNode i = staticContextData.getRegionStart(); i != null; i = i.getPrevious())
            if (i.getType() != LABEL && i.getType() != LINE)
                index++;
        
        return thisClassName() + SUBSEP + thisMethodName() + SUBSEP + thisMethodDescriptor() + SUBSEP + index;
    }
    
    public String getReflectiveAllocationSite() {
        return thisClassName() + SUBSEP + thisMethodName() + SUBSEP + thisMethodDescriptor() + SUBSEP + OUT_OF_LINE_INDEX;
    }
}
