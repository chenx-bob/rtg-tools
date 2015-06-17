/*
 * Copyright (c) 2014. Real Time Genomics Limited.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rtg.util.format;

/**
 * A holder for float values - intended to be subclassed as necessary
 * to carry different output formats for rendering etc.
 *
 */
public class FloatValue implements FormattedValue, Comparable<FormattedValue> {

  protected final float mValue;

  FloatValue(final float v) {
    mValue = v;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    toString(sb);
    return sb.toString();
  }


  @Override
  public void toString(final StringBuilder sb) {
    sb.append(mValue);
  }


  @Override
  public int maxLength() {
    return Integer.MAX_VALUE;
  }


  @Override
  public int compareTo(final FormattedValue obj) {
    if (obj == this) {
      return 0;
    }
    if (obj instanceof NullValue) {
      return +1;
    }
    final FloatValue fv = (FloatValue) obj;
    final float v = fv.mValue;
    if (mValue < v) {
      return -1;
    } else if (mValue > v) {
      return +1;
    } else {
      return 0;
    }
  }

  @Override
  public boolean equals(final Object obj) {
    return obj instanceof FloatValue && mValue == ((FloatValue) obj).mValue;
  }

  @Override
  public int hashCode() {
    return Float.floatToRawIntBits(mValue);
  }
}

