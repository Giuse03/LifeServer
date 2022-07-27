package ezmessage;


public class TextReplacer {

    private String match, replace, text;

    public TextReplacer setText(String text) {
        this.text = text;
        return this;
    }

    public TextReplacer match(String match) {
        this.match = match;
        return this;
    }

    public TextReplacer replaceWith(String replace) {
        this.replace = replace;
        return this;
    }


    public String returnReplace() {
        return text.replace(match, replace);
    }

}
