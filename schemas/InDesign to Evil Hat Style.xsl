<?xml version="1.0"?>

<!--
    Document   : InDesign Export.xsl
    Created on : October 30, 2015, 9:00 AM
    Author     : plewis
    Description:
        Remove the Root, Story, and p1 elements. Translate ul into
        a list.
-->
     
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <!-- Root match. -->
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- Remove these elements. -->    
    <xsl:template match="Story|Root">
        <xsl:apply-templates/>
    </xsl:template>    
    
    <!-- Add a newline after these elements and remove the element. -->
    <xsl:template match="p1|li|l1|l2">
        <xsl:text>&#xa;</xsl:text>
        <xsl:apply-templates select="@*|node()"/>	
    </xsl:template>
    
    <!-- Add a tab after these elements and remove the element. -->
    <xsl:template match="Cell">
        <xsl:apply-templates/>
        <xsl:text>&#x09;</xsl:text>
    </xsl:template>
	
    <!-- Add a newline after these elements (keep element). -->
    <xsl:template match="chapter">
        <xsl:text>&#xa;</xsl:text>
        <xsl:element name="chapter">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="h1">
        <xsl:text>&#xa;</xsl:text>
        <xsl:element name="h1">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>    
    
    <xsl:template match="h2">
        <xsl:text>&#xa;</xsl:text>
        <xsl:element name="h2">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template> 
    
    <!-- Rename h1nb to h1. -->
    <xsl:template match="h1nb">
        <xsl:text>&#xa;</xsl:text>
        <h1>
            <xsl:apply-templates select="@*|node()"/>
        </h1>
    </xsl:template>    
    
    <!-- Rename ul to list. -->
    <xsl:template match="ul">
        <xsl:text>&#xa;</xsl:text>
        <list>
            <xsl:apply-templates select="@*|node()"/>
        <xsl:text>&#xa;</xsl:text>    
        </list>
    </xsl:template>
    
    <!-- Rename Table to table. -->
    <xsl:template match="Table">
        <xsl:text>&#xa;</xsl:text>
        <table>
            <xsl:apply-templates select="node()"/>
            <xsl:text>&#xa;</xsl:text>
        </table>
    </xsl:template>
    
    <xsl:template match="statblock/text()|cellp/text()">
        <xsl:value-of select="normalize-space()"/>
    </xsl:template>
    
    <!-- Remove attributes from b. -->
    <xsl:template match="b">
        <b>
            <xsl:apply-templates select="node()"/>
        </b>
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
