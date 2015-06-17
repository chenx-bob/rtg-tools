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
package com.rtg.sam;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import junit.framework.TestCase;

/**
 */
public class DefaultSamFilterTest extends TestCase {


  public void testFilterRecordByFlags() {
    final SamFilterParams.SamFilterParamsBuilder builder = SamFilterParams.builder();
    final SAMRecord rec = new SAMRecord(new SAMFileHeader()); // Not unmapped but alignment position == 0
    rec.setReadUnmappedFlag(true);      // Unmapped with alignment position == 0
    assertTrue(DefaultSamFilter.acceptRecord(builder.create(), rec));
    builder.excludeUnmapped(true);
    assertFalse(DefaultSamFilter.acceptRecord(builder.create(), rec));

    rec.setReadUnmappedFlag(false);      // Mapped with alignment position == 10
    rec.setAlignmentStart(10);
    assertTrue(DefaultSamFilter.acceptRecord(builder.create(), rec));

    rec.setDuplicateReadFlag(true);      // Now a duplicate
    assertTrue(DefaultSamFilter.acceptRecord(builder.create(), rec));
    builder.excludeDuplicates(true);
    assertFalse(DefaultSamFilter.acceptRecord(builder.create(), rec));
    builder.excludeDuplicates(false);

    rec.setReadPairedFlag(true); // Now paired-end

    builder.excludeUnmated(true);
    assertFalse(DefaultSamFilter.acceptRecord(builder.create(), rec));
    rec.setProperPairFlag(true); // Now properly paired (i.e. no longer unmated
    assertTrue(DefaultSamFilter.acceptRecord(builder.create(), rec));

    builder.excludeMated(true);
    assertFalse(DefaultSamFilter.acceptRecord(builder.create(), rec));

  }

  public void testFilterUnplaced() {
    final SamFilterParams.SamFilterParamsBuilder builder = SamFilterParams.builder().excludeUnplaced(true);
    final SAMRecord rec = new SAMRecord(new SAMFileHeader()); // Not unmapped but alignment position == 0
    assertFalse(DefaultSamFilter.acceptRecord(builder.create(), rec));
    rec.setReadUnmappedFlag(true);      // Unmapped with alignment position == 0
    assertFalse(DefaultSamFilter.acceptRecord(builder.create(), rec));
  }

}
