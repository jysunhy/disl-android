/* Software Name : AsmDex
 * Version : 1.0
 *
 * Copyright © 2012 France Télécom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.ow2.asmdex.tree;

import java.util.Map;

import org.ow2.asmdex.MethodVisitor;

/**
 * A node that represents a local variable instruction, using a Long variable.
 * A local variable instruction is an instruction that loads or stores the value
 * of a local variable.
 * 
 * @author Julien Névo, based on the ASM framework.
 */
public class VarInsnLongNode extends AbstractInsnNode {

    /**
     * The operand of this instruction. This operand is either a value or a source Register.
     */
    public long var;
    
    /**
     * The destination register.
     */
    public int destinationRegister;

    /**
     * Constructs a new {@link VarInsnLongNode}.
     * 
     * @param opcode the opcode of the local variable instruction to be
     *        constructed. This opcode is either CONST-WIDE, CONST-WIDE/HIGH16.
     * @param destinationRegister the destination register. 
     * @param var the operand of the instruction to be constructed. This operand
     *        is the index of a local variable.
     */
    public VarInsnLongNode(final int opcode, final int destinationRegister, final long var) {
        super(opcode);
        this.destinationRegister = destinationRegister;
        this.var = var;
    }

    /**
     * Sets the opcode of this instruction.
     * 
     * @param opcode the new instruction opcode.
     * 		  This opcode is either CONST-WIDE, CONST-WIDE/HIGH16.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return VAR_INSN_LONG;
    }

    @Override
    public void accept(final MethodVisitor mv) {
        mv.visitVarInsn(opcode, destinationRegister, var);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> labels) {
        return new VarInsnLongNode(opcode, destinationRegister, var);
    }
}