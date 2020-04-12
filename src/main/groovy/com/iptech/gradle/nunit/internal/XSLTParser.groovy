package com.iptech.gradle.nunit.internal

import groovy.transform.CompileStatic
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider

import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@CompileStatic
class XSLTParser {
    final RegularFileProperty failuresXSLT
    final RegularFileProperty failureCountsXSLT
    final RegularFileProperty summaryXSLT
    final RegularFileProperty reportXSLT

    XSLTParser(ObjectFactory objectFactory) {
        failuresXSLT = objectFactory.fileProperty()
        failureCountsXSLT = objectFactory.fileProperty()
        summaryXSLT = objectFactory.fileProperty()
        reportXSLT = objectFactory.fileProperty()
    }

    void setFailuresXSLT(File value) {
        failuresXSLT.set(value)
    }

    void setFailuresXSLT(RegularFile value) {
        failuresXSLT.set(value)
    }

    void setFailuresXSLT(Provider<? extends RegularFile> value) {
        failuresXSLT.set(value)
    }

    void setFailureCountsXSLT(File value) {
        failureCountsXSLT.set(value)
    }

    void setFailureCountsXSLT(RegularFile value) {
        failureCountsXSLT.set(value)
    }

    void setFailureCountsXSLT(Provider<? extends RegularFile> value) {
        failureCountsXSLT.set(value)
    }

    void setSummaryXSLT(File value) {
        summaryXSLT.set(value)
    }

    void setSummaryXSLT(RegularFile value) {
        summaryXSLT.set(value)
    }

    void setSummaryXSLT(Provider<? extends RegularFile> value) {
        summaryXSLT.set(value)
    }

    void setReportXSLT(File value) {
        reportXSLT.set(value)
    }

    void setReportXSLT(RegularFile value) {
        reportXSLT.set(value)
    }

    void setReportXSLT(Provider<? extends RegularFile> value) {
        reportXSLT.set(value)
    }

    Integer parseFailureCounts(File file) {
        String xslt = failureCountsXSLT.isPresent() ? failureCountsXSLT.get().asFile.text : loadResource('/nunit/text-summary-failures-count.xslt').getText()
        String res = parse(file, xslt)
        if(res) {
            return Integer.parseInt(res)
        }
        throw new Exception('Could not parse failures from xml file')
    }

    String parseFailures(File file) {
        String xslt = failuresXSLT.isPresent() ? failuresXSLT.get().asFile.text : loadResource('/nunit/text-summary-failures.xslt').getText()
        return parse(file, xslt)
    }

    String parseSummary(File file) {
        String xslt = summaryXSLT.isPresent() ? summaryXSLT.get().asFile.text : loadResource('/nunit/text-summary.xslt').getText()
        return parse(file, xslt)
    }

    String parseReport(File file) {
        String xslt = reportXSLT.isPresent() ? reportXSLT.get().asFile.text : loadResource('/nunit/text-report.xslt').getText()
        return parse(file, xslt)
    }

    String parse(File xmlFile, String xsltText) {
        return parse(xmlFile.text, xsltText)
    }

    String parse(String xml, String xsltText) {
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream()
        Transformer t = TransformerFactory.newInstance().newTransformer(new StreamSource(new StringReader(xsltText)))
        t.transform(new StreamSource(new StringReader(xml)), new StreamResult(htmlStream))
        return new ByteArrayInputStream((byte[])htmlStream.toByteArray()).text
    }

    private URL loadResource(String name) {
        return getClass().getResource(name)
    }
}
