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

package com.rtg.vcf.eval;

import java.util.Locale;

import com.rtg.vcf.VariantType;
import com.rtg.vcf.VcfRecord;
import com.rtg.vcf.VcfUtils;

/**
 * Interface defining ROC filters
 */
public enum RocFilter {


  /** accepts everything **/
  ALL {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return true;
    }

    @Override
    public String fileName() {
      return "weighted_roc.tsv";
    }
    @Override
    boolean requiresGt() {
      return false;
    }
  },
  /** all homozygous **/
  HOM("homozygous") {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return VcfUtils.isHomozygousAlt(gt);
    }
  },
  /** all heterozygous **/
  HET("heterozygous") {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return VcfUtils.isHeterozygous(gt);
    }
  },
  /** all SNPs **/
  SNP {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      final VariantType type = VariantType.getType(rec, gt);
      return type == VariantType.SNP;
    }
  },
  /** non-SNPs **/
  NON_SNP {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      final VariantType type = VariantType.getType(rec, gt);
      return type != VariantType.SNP;
    }
  },
  /** all MNPs (non-length changing) **/
  MNP {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      final VariantType type = VariantType.getType(rec, gt);
      return type == VariantType.MNP;
    }
  },
  /** all indels (length changing) **/
  INDEL {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      final VariantType type = VariantType.getType(rec, gt);
      return type.isIndelType();
    }
  },

  // RTG simple vs complex breakdowns
  /** all RTG complex calls **/
  XRX {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return VcfUtils.isComplexScored(rec);
    }
    @Override
    boolean requiresGt() {
      return false;
    }
  },
  /** all RTG simple (non complex) calls **/
  NON_XRX {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return !VcfUtils.isComplexScored(rec);
    }
    @Override
    boolean requiresGt() {
      return false;
    }
  },
  /** homozygous complex calls **/
  HOM_XRX("homozygous_xrx") {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return VcfUtils.isComplexScored(rec) && VcfUtils.isHomozygousAlt(gt);
    }
  },
  /** homozygous simple (non complex) calls **/
  HOM_NON_XRX("homozygous_non_xrx") {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return !VcfUtils.isComplexScored(rec) && VcfUtils.isHomozygousAlt(gt);
    }
  },
  /** heterozygous complex calls **/
  HET_XRX("heterozygous_xrx") {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return VcfUtils.isComplexScored(rec) && VcfUtils.isHeterozygous(gt);
    }
  },
  /** heterozygous simple (non complex) calls **/
  HET_NON_XRX("heterozygous_non_xrx") {
    @Override
    boolean accept(VcfRecord rec, int[] gt) {
      return !VcfUtils.isComplexScored(rec) && VcfUtils.isHeterozygous(gt);
    }
  };

  /** The filename extension used for all ROC files */
  public static final String ROC_EXT = "_roc.tsv";

  private final String mBaseFilename;

  RocFilter() {
    this(null);
  }

  RocFilter(String baseFilename) {
    mBaseFilename = baseFilename;
  }

  /**
   * @return true if the filter requires access to a GT value
   */
  boolean requiresGt() {
    return true;
  }

  /**
   * Tests specified record
   * @param rec record to be tested
   * @param sample sample number
   * @return if accepted returns true, false otherwise
   */
  boolean accept(VcfRecord rec, int sample) {
    final int[] gt = VcfUtils.getValidGt(rec, sample);
    return accept(rec, gt);
  }

  /**
   * Tests specified record
   * @param rec record to be tested
   * @param gt the split GT field of the sample
   * @return if accepted returns true, false otherwise
   */
  abstract boolean accept(VcfRecord rec, int[] gt);

  /**
   * Get the name of the default output file for this filter
   * @return the output file name
   */
  public String fileName() {
    if (mBaseFilename != null) {
      return mBaseFilename + ROC_EXT;
    } else {
      return name().toLowerCase(Locale.ROOT) + ROC_EXT;
    }
  }
}
