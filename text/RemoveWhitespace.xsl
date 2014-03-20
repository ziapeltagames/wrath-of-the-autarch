<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : RemoveWhitespace.xsl
    Created on : March 19, 2014, 7:13 PM
    Author     : plewis
    Description:
        Used to clean up files prior to importing into InDesign.
        Must conform to the BookChapter.dtd schema.
        For some reason the general (and easy to use) remove-whitespace
        XSLT directive isn't working. Instead, had to manually match
        the offending sections.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="Story/text()|Root/text()|Table/text()|Cell/text()|statblock/text()|cellp/text()">
        <xsl:value-of select="normalize-space()"/>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:for-each select="@*">
                <xsl:copy/>
            </xsl:for-each>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>
