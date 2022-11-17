package de.fhg.iais.roberta.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.owasp.html.HtmlChangeListener;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilForHtmlXml {
    private static final Logger LOG = LoggerFactory.getLogger(UtilForHtmlXml.class);
    private static final Pattern GET_DESCRIPTION = Pattern.compile("description=\".*?\"");

    private static final HtmlChangeListener<List<String>> HTML_CHANGE_LISTENER = new HtmlChangeListener<List<String>>() {
        @Override
        public void discardedTag(List<String> arg0, String tagName) {
            UtilForHtmlXml.LOG.debug("Discarding tag: " + tagName);
        }

        @Override
        public void discardedAttributes(List<String> arg0, String arg1, String... attributes) {
            UtilForHtmlXml.LOG.debug("Discarding attribute of: " + arg1);
        }
    };

    private static final String[] TAG_WHITE_LIST =
        {
            "b",
            "i",
            "u",
            "strike",
            "blockquote",
            "span",
            "em",
            "div",
            "font",
            "pre",
            "br",
            "ul",
            "ol",
            "li",
            "h1",
            "h2",
            "h3",
            "h4",
            "h5",
            "h6",
            "p",
            "strong",
            "font"
        };
    private static final String[] ATTRIBUTE_WHITE_LIST =
        {
            "size",
            "class",
            "id",
            "style",
            "color",
            "align",
            "font"
        };

    private static final PolicyFactory SANITIZER =
        new HtmlPolicyBuilder()
            .allowElements(TAG_WHITE_LIST)
            .allowWithoutAttributes("span")
            .allowAttributes(ATTRIBUTE_WHITE_LIST)
            .onElements(TAG_WHITE_LIST)
            .toFactory();

    public static String checkProgramTextForXSS(String programText) {
        if ( programText == null ) {
            return programText;
        }
        Matcher matcher = GET_DESCRIPTION.matcher(programText);
        String description;
        try {
            matcher.find();
            description = matcher.group();
        } catch ( IllegalStateException e ) {
            return programText;
        }
        String newDescription = description.split("description=\"")[1];
        newDescription = newDescription.substring(0, newDescription.length() - 1);
        if ( newDescription.length() == 0 ) {
            return programText;
        }
        String safeHTML = UtilForHtmlXml.removeUnwantedDescriptionHTMLTags(newDescription);
        if ( !safeHTML.equals(newDescription) ) {
            return programText.replace(description, "description=\"" + safeHTML + "\"");
        } else {
            return programText;
        }
    }

    /**
     * Remove unwanted tags and tag/attribute combinations from a string to prevent XSS
     *
     * @param input XML code containing description attribute that contains code with unwanted tags
     * @return output XML code without unwanted tags in description attribute
     */
    public static String removeUnwantedDescriptionHTMLTags(String input) {
        input = StringEscapeUtils.unescapeXml(input);
        List<String> results = new ArrayList<>();
        String safeHTML = SANITIZER.sanitize(input, HTML_CHANGE_LISTENER, results);
        return StringEscapeUtils.escapeXml11(safeHTML);
    }
}
