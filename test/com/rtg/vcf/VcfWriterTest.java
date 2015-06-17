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

import static com.rtg.util.StringUtils.TAB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.rtg.vcf.header.VcfHeader;

import junit.framework.TestCase;

/**
 */
public class VcfWriterTest extends TestCase {

  private static final String LS = "\n"; // Not platform specific

  public VcfWriterTest(String name) {
    super(name);
  }

  public void test() throws IOException {
    final VcfHeader head = new VcfHeader();
    head.setVersionValue(VcfHeader.VERSION_VALUE);
    head.addMetaInformationLine("##test1212121")
    .addMetaInformationLine("##test12");
    head.addSampleName("sample1")
    .addSampleName("sample2");

    final VcfRecord rec = new VcfRecord();
    rec.setSequence("chr1")
    .setStart(1209)
    .setId(".")
    .setQuality("12.8")
    .setRefCall("a")
    .addAltCall("c")
    .addAltCall("t")
    .addFilter("TEST1")
    .addFilter("TEST2")
    .addInfo("DP", "23")
    .addInfo("TEST", "45,46,47,48")
    .setNumberOfSamples(2)
    .addFormatAndSample("GT", "0/0")
    .addFormatAndSample("GT", "0/1")
    .addFormatAndSample("GQ", "100")
    .addFormatAndSample("GQ", "95")
    ;

    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final VcfWriter w = new VcfWriter(head, bos);
    w.write(rec);
    w.write(rec);

    final String line = ""
      + "chr1" + TAB
      + "1210" + TAB
      + "." + TAB
      + "a" + TAB
      + "c,t" + TAB
      + "12.8" + TAB
      + "TEST1;TEST2" + TAB
      + "DP=23;TEST=45,46,47,48" + TAB
      + "GT:GQ" + TAB
      + "0/0:100" + TAB
      + "0/1:95" + LS;
    final String exp = "##fileformat=VCFv4.1" + LS
      + "##test1212121" + LS
      + "##test12" + LS
      + "#CHROM" + TAB + "POS" + TAB + "ID" + TAB + "REF" + TAB + "ALT" + TAB + "QUAL" + TAB + "FILTER" + TAB + "INFO" + TAB + "FORMAT" + TAB + "sample1" + TAB + "sample2" + LS
      + line
      + line;

    assertEquals(exp, bos.toString());
    assertEquals(w.getHeader(), head);
  }

  public void testErrors() {
    try {
      new VcfWriter(null, new ByteArrayOutputStream());
    } catch (NullPointerException ex) {
      assertEquals("header cannot be null", ex.getMessage());
    }

    try {
      new VcfWriter(new VcfHeader(), null);
    } catch (NullPointerException ex) {
      assertEquals("output stream cannot be null", ex.getMessage());
    }
  }
}
