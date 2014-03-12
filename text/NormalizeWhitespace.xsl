<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : Normalize.xsl
    Created on : March 10, 2014, 11:24 PM
    Author     : plewis
    Description:
        Removes whitespace from XML. Useful for importing into InDesign.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates/> 
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:for-each select="@*">
                <xsl:copy/>
            </xsl:for-each>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space()"/>
    </xsl:template>

</xsl:stylesheet>
