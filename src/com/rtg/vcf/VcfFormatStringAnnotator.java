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

package com.rtg.vcf;

import com.rtg.vcf.annotation.AbstractDerivedFormatAnnotation;
import com.rtg.vcf.header.FormatField;
import com.rtg.vcf.header.MetaType;
import com.rtg.vcf.header.VcfHeader;
import com.rtg.vcf.header.VcfNumber;

/**
 */
public class VcfFormatStringAnnotator implements VcfAnnotator {

  final AbstractDerivedFormatAnnotation mAnnotation;

  /**
   * Create an FORMAT annotation that outputs a string value.
   * @param annotation the annotation to use.
   */
  public VcfFormatStringAnnotator(AbstractDerivedFormatAnnotation annotation) {
    assert annotation != null && annotation.getType().getClassType() == String.class;
    mAnnotation = annotation;
  }

  @Override
  public void updateHeader(VcfHeader header) {
    header.ensureContains(new FormatField(mAnnotation.getName(), MetaType.STRING, new VcfNumber("1"), mAnnotation.getDescription()));
  }

  @Override
  public void annotate(VcfRecord rec) {
    for (int i = 0; i < rec.getNumberOfSamples(); i++) {
      final String val = (String) mAnnotation.getValue(rec, i);
      if (val != null) {
        rec.setFormatAndSample(mAnnotation.getName(), val, i);
      }
    }
    rec.padFormatAndSample(mAnnotation.getName());
  }

}
