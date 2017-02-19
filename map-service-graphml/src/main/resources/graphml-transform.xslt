<xsl:stylesheet version="2.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xpath-default-namespace="http://graphml.graphdrawing.org/xmlns">

	<xsl:output method="xml" indent="yes" />

	<xsl:variable name="keysAttrsNameArray" as="xs:string*">
		<xsl:text>Owner</xsl:text>
		<xsl:text>Type</xsl:text>
		<xsl:text>Units</xsl:text>
		<xsl:text>IsStartPoint</xsl:text>
	</xsl:variable>

	<xsl:variable name="keysList"
		select="/graphml/key[@attr.name=$keysAttrsNameArray]" />

	<xsl:template match="/">
		<graphml xmlns="http://graphml.graphdrawing.org/xmlns"
			xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<xsl:copy-of copy-namespaces="no" select="$keysList" />
			<xsl:apply-templates select="graphml/graph" />
		</graphml>
	</xsl:template>

	<xsl:template match="graph">
		<xsl:copy copy-namespaces="no">
			<xsl:for-each select="@*">
				<xsl:copy />
			</xsl:for-each>
			<xsl:apply-templates select="node|edge" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="node|edge">
		<xsl:copy copy-namespaces="no">
			<xsl:for-each select="@*">
				<xsl:copy />
			</xsl:for-each>
			<xsl:apply-templates select="data[@key=$keysList/@id]" />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="data[@key=$keysList/@id]">
		<xsl:copy-of select="." copy-namespaces="no" />
	</xsl:template>



</xsl:stylesheet>