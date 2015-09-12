/*
 * Copyright 2013-2015 Mikhail Shugay (mikhail.shugay@gmail.com)
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

package com.antigenomics.higblast.genomic

import com.antigenomics.higblast.blast.BlastInstanceFactory
import com.antigenomics.higblast.io.Read
import com.antigenomics.higblast.mapping.ReadMappingDetailsProvider
import org.junit.AfterClass
import org.junit.Test

class ReadMappingDetailsProviderTest {
    private final BlastInstanceFactory factory

    ReadMappingDetailsProviderTest() {
        factory = new BlastInstanceFactory("data/", "human", ["IGH"], true, false)
        factory.annotateV()
    }

    @Test
    void fullLengthTest() {
        def instance = factory.create()

        def seq = "TAAGAGGGCAGTGGTATCAACGCAGAGTACGGATATTCTGAGGTCCGCTC" +
                "TCTTGGGGGGCTTTCTGAGAGTCGTGGATCTCATGTGCAAGAAAATGAAG" +
                "CACCTGTGGTTCTTCCTCCTGCTGGTGGCGGCTCCCAGATGGGTCCTGTC" +
                "CCAGCTGCAGCTGCAGGAGTCGGGCCCAGGACTGGTGAAGCCCTCGGAGA" +
                "CCCTGTCCCTCAGGTGCACTGTCTCTGGGGGCTCCATGACAAGAACTACT" +
                "GACTACTGGGGCTGGGTCCGCCAGCCCCCAGGGAAGGGACTGGAGTGGAT" +
                "TGCAAGTGTCTCTTATAGTGGGAGCACCACCTACAACCCGTCCCGGAAGA" +
                "GTCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAA" +
                "CTGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTCTGTGCGAGGTG" +
                "GCTTGGGGAAGACATTCGGACCTTTGACTCCTGGGGCCAGGGAACCCTGG" +
                "TCACCGTCTCTAA"

        instance.put(new Read(null, seq))
        instance.put(null)

        def readMapping = instance.take()

        def details = ReadMappingDetailsProvider.getDetails(readMapping)

        assert details.fr1 == "CAGCTGCAGCTGCAGGAGTCGGGCCCAGGACTGGTGAAGCCCTCGGAGACCCTGTCCCTCAGGTGCACTGTCTCT"
        assert details.cdr1 == "GGGGGCTCCATGACAAGAACTACTGACTAC"
        assert details.fr2 == "TGGGGCTGGGTCCGCCAGCCCCCAGGGAAGGGACTGGAGTGGATTGCAAGT"
        assert details.cdr2 == "GTCTCTTATAGTGGGAGCACC"
        assert details.fr3 == "ACCTACAACCCGTCCCGGAAGAGTCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAACTGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTC"
        assert details.contig == "CAGCTGCAGCTGCAGGAGTCGGGCCCAGGACTGGTGAAGCCCTCGGAGAC" +
                "CCTGTCCCTCAGGTGCACTGTCTCTGGGGGCTCCATGACAAGAACTACTG" +
                "ACTACTGGGGCTGGGTCCGCCAGCCCCCAGGGAAGGGACTGGAGTGGATT" +
                "GCAAGTGTCTCTTATAGTGGGAGCACCACCTACAACCCGTCCCGGAAGAG" +
                "TCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAAC" +
                "TGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTCTGTGCGAGGTGG" +
                "CTTGGGGAAGACATTCGGACCTTTGACTCCTGGGGCCAGGGAACCCTGGT" +
                "CACCGTCTCTAA"
        
        instance.close()
    }

    @Test
    void partialTest() {
        def instance = factory.create()

        def seq = "CTGGGTCCGCCAGCCCCCAGGGAAGGGACTGGAGTGGATTGCAAGTGTCT" +
                "CTTATAGTGGGAGCACCACCTACAACCCGTCCCGGAAGAGTCGAGTCACA" +
                "ATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAACTGAGGTCCAT" +
                "GACCGCCGCAGACACGGCTGTGTATTTCTGTGCGAGGTGGCTTGGGGAAG" +
                "ACATTCGGACCTTTGACTCCTGGGGCCAGGGAACCCTGGTCACCGTCTCT" +
                "AA"

        instance.put(new Read(null, seq))
        instance.put(null)

        def readMapping = instance.take()

        def details = ReadMappingDetailsProvider.getDetails(readMapping)

        assert details.fr1 == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        assert details.cdr1 == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        assert details.fr2 == "NNNNNCTGGGTCCGCCAGCCCCCAGGGAAGGGACTGGAGTGGATTGCAAGT"
        assert details.cdr2 == "GTCTCTTATAGTGGGAGCACC"
        assert details.fr3 == "ACCTACAACCCGTCCCGGAAGAGTCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAACTGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTC"
        assert details.contig == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" +
                "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" +
                "NNNNNNNNNNCTGGGTCCGCCAGCCCCCAGGGAAGGGACTGGAGTGGATT" +
                "GCAAGTGTCTCTTATAGTGGGAGCACCACCTACAACCCGTCCCGGAAGAG" +
                "TCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAAC" +
                "TGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTCTGTGCGAGGTGG" +
                "CTTGGGGAAGACATTCGGACCTTTGACTCCTGGGGCCAGGGAACCCTGGT" +
                "CACCGTCTCTAA"

        instance.close()
    }

    @Test
    void partialTest2() {
        def instance = factory.create()

        def seq = "ATTATAGTGGGAGCACCACCTACAACCCGTCCCGGAAGAGTCGAGTCACA" +
                "ATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAACTGAGGTCCAT" +
                "GACCGCCGCAGACACGGCTGTGTATTTCTGTGCGAGGTGGCTTGGGGAAG" +
                "ACATTCGGACCTTTGACTCCTGGGGCCAGGGAACCCTGGTCACCGTCTCT" +
                "AA"

        instance.put(new Read(null, seq))
        instance.put(null)

        def readMapping = instance.take()

        def details = ReadMappingDetailsProvider.getDetails(readMapping)

        assert details.fr1 == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        assert details.cdr1 == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        assert details.fr2 == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        assert details.cdr2 == "NNNNATTATAGTGGGAGCACC"
        assert details.fr3 == "ACCTACAACCCGTCCCGGAAGAGTCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAACTGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTC"
        assert details.contig == "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" +
                "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" +
                "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" +
                "NNNNNNNNNNATTATAGTGGGAGCACCACCTACAACCCGTCCCGGAAGAG" +
                "TCGAGTCACAATCTCCCTAGACCCGTCCAGGAACGAACTCTCCCTGGAAC" +
                "TGAGGTCCATGACCGCCGCAGACACGGCTGTGTATTTCTGTGCGAGGTGG" +
                "CTTGGGGAAGACATTCGGACCTTTGACTCCTGGGGCCAGGGAACCCTGGT" +
                "CACCGTCTCTAA"

        instance.close()
    }

    @AfterClass
    static void tearDown() {
        SegmentDatabase.clearTemporaryFiles()
    }
}