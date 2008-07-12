package ru.yandex.market.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import ru.yandex.market.CategoryTree;
import ru.yandex.utils.Pair;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class CategoryTreeDaoXml implements CategoryTreeDao {

    private static final Logger logger = Logger.getLogger(CategoryTreeDaoXml.class);

    private static final int BUFFER_SIZE = 256 * 1024;

    public Map<CategoryTree.CategoryTreeNode, Integer> loadCategoryTree() {

        logger.debug("CategoryTreeDaoXml : loadCategoryTree()");

        // prepare map
        final Map<CategoryTree.CategoryTreeNode, Integer> result = new HashMap<CategoryTree.CategoryTreeNode, Integer>();
        final ContentHandler handler = new DefaultHandler() {

            private static final String TAG = "category";
            private static final String YES = "Yes";

            private boolean inInterest;
            private String name;
            private String uniqName;
            private int id;
            private int hyperId;
            private int parentId;
            private boolean visible;

            @Override
            public void startElement(final String uri, final String localName, final String qname, final Attributes attributes) {
                if (TAG.equals(localName)) {
                    name = attributes.getValue("name");
                    uniqName = attributes.getValue("uniq_name");
                    id = defaultInt(attributes.getValue("id"));
                    hyperId = defaultInt(attributes.getValue("hyper_id"));
                    parentId = defaultInt(attributes.getValue("parent"));
                    visible = true;
                    if(attributes.getValue("not_used") != null) {
                    	visible = !YES.equals(attributes.getValue("not_used"));
                    }
                    inInterest = true;
                }
            }

            @Override
            public void endElement(final String uri, final String localName, final String qname) {
                if (TAG.equals(localName)) {
                    pushData();
                    inInterest = false;
                }
            }

            @Override
            public void characters(final char[] ch, final int start, final int length) {
                // do nothing
            }

            @Override
            public void ignorableWhitespace(final char ch[], final int start, final int length) throws SAXException {
                characters(ch, start, length);
            }

            private void pushData() {
                final CategoryTree.CategoryTreeNode node = new CategoryTree.CategoryTreeNode(name, uniqName, hyperId, id, visible);
                result.put(node, parentId);
            }
        };

        processXml(handler, loadCategoryTreeFileName);

        return result;
    }

    public Map<Integer, Pair<Integer, List<Integer>>> getHyperToMatcherMap() {
        logger.debug("getHyperToMatcherMap()");

        final Map<Integer, Pair<Integer, List<Integer>>> result = new HashMap<Integer, Pair<Integer, List<Integer>>>();

        final ContentHandler handler = new DefaultHandler() {

            private static final String TAG = "tids";

            private boolean inInterest;
            private String hid;
            private String link;
            private StringBuilder content = new StringBuilder();

            @Override
            public void startElement(final String uri, final String localName, final String qname, final Attributes attributes) {
                if (TAG.equals(localName)) {
                    hid = attributes.getValue("hid");
                    link = attributes.getValue("link");
                    inInterest = true;
                }
            }

            @Override
            public void endElement(final String uri, final String localName, final String qname) {
                if (TAG.equals(localName)) {
                    pushData();
                    content = new StringBuilder();
                    inInterest = false;
                }
            }

            @Override
            public void characters(final char[] ch, final int start, final int length) {
                if (inInterest) {
                    content.append(ch, start, length);
                }
            }

            @Override
            public void ignorableWhitespace(final char ch[], final int start, final int length) throws SAXException {
                characters(ch, start, length);
            }

            private void pushData() {
                final String categories = content.toString();
                final List<Integer> list = new ArrayList<Integer>();
                if (categories != null) {
                    final StringTokenizer st = new StringTokenizer(categories, ", ");
                    while (st.hasMoreTokens()) {
                        list.add(Integer.valueOf(st.nextToken()));
                    }
                }
                final Integer a_link = defaultInt(link);
                final Integer a_hid = defaultInt(hid);
                result.put(a_link, Pair.makePair(a_hid, list));
            }
        };
        processXml(handler, hyperToMatcherMapFileName);

        return result;
    }

    private String buildFullPath(final String fileName) {
        return xmlFilePath + "/" + fileName;
    }

    private void processXml(final ContentHandler handler, final String fileName) {
        final String fullFileName = buildFullPath(fileName);
        final String msg = "Parsing file: '" + fullFileName + "'";
        logger.debug(msg);
        final long TS_start = System.currentTimeMillis();
        try {
            final XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            final InputStream in = new BufferedInputStream(new FileInputStream(fullFileName), BUFFER_SIZE);
            parser.parse(new InputSource(in));
            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            final long TS_finish = System.currentTimeMillis();
            logger.debug(msg + String.format(" finished, took %s seconds.", (TS_finish - TS_start) / 1000d));
        }
    }

    private static int defaultInt(final String val) {
        return (val == null) ? 0 : Integer.valueOf(val);
    }

    // ----------------------- bean related properties -----------------------------

    private String xmlFilePath = "/var/www/marketindexer/mbo-import";

    @Required
    public void setXmlFilePath(final String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    private String loadCategoryTreeFileName = "super-controller.tovar_categories.xml";

    @Required
    public void setLoadCategoryTreeFileName(final String loadCategoryTreeFileName) {
        this.loadCategoryTreeFileName = loadCategoryTreeFileName;
    }

    private String hyperToMatcherMapFileName = "super-controller.map_hyper_to_matcher.xml";

    @Required
    public void setHyperToMatcherMapFileName(final String hyperToMatcherMapFileName) {
        this.hyperToMatcherMapFileName = hyperToMatcherMapFileName;
    }

}
