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
package com.rtg.util;

import java.io.File;

import com.rtg.util.io.TestDirectory;

import junit.framework.TestCase;

/**
 */
public class HtmlReportHelperTest extends TestCase {

  public void testBits() throws Exception {
    try (TestDirectory dir = new TestDirectory()) {
      final HtmlReportHelper hrh = new HtmlReportHelper(dir, "report");
      assertEquals("report_files", hrh.getResourcesDirName());
      assertEquals("report.html", hrh.getReportFile().getName());
      assertEquals(dir, hrh.getReportFile().getParentFile());
      assertEquals(dir, hrh.getBaseDir());

      hrh.copyResources("com/rtg/util/resources/krona.xml");
      final File repDir = new File(dir, hrh.getResourcesDirName());
      assertEquals(hrh.getResourcesDir(), repDir);
      assertTrue(repDir.exists());
      final File f = new File(repDir, "krona.xml");
      assertTrue(f.exists());
    }
  }
}
