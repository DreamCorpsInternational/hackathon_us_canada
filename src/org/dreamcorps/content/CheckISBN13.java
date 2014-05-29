package org.dreamcorps.content;

/**
 * Class that models a 13-digit ISBN code.
 *   @author davereed
 *   @version 9/09/11
 */
public class CheckISBN13 implements Comparable<CheckISBN13>{

    private String rawCode;         // the original String repr. of the code
    private String strippedCode;    // the code with dashes stripped out
    private boolean isValid;        // flag as to whether the code is valid

    /**
     * Constructs an ISBN13 object given its String representation (including
     * dashes).  If exactly one '?' appears in the String, it is replaced with 
     * the appropriate digit. 
     *   @param str theString representation of the code
     */
    public CheckISBN13(String str) {
        this.rawCode = str.trim();
        this.strippedCode = this.rawCode.replaceAll("-", "");
        this.isValid = false;

        int loc = this.strippedCode.indexOf('?');
        if (loc == -1) {
            this.isValid = this.validCheck(this.strippedCode);
        }
        else {
            for (int i = 0; i <= 9; i++) {
                String substCode = this.strippedCode.substring(0, loc) +
                                    (char)(i+'0') +
                                    this.strippedCode.substring(loc+1);
                if (this.validCheck(substCode)) {
                    this.rawCode = this.rawCode.replace("?", ""+(char)(i+'0'));
                    this.strippedCode = substCode;
                    this.isValid = true;
                    break;
                }
            }
        }
    }

    /**
     * Private helper method for determining whether a code is valid.
     *   @param code the ISBN code to be checked
     *   @return true if a valid code, otherwise false
     */
    private boolean validCheck(String code) {
        if (code.length() != 13) {
            return false;
        }
        for (int i = 0; i < code.length(); i++) {
            if (!Character.isDigit(code.charAt(i))) {
                return false;
            }
        }
        int check = (10 - (  (code.charAt(0) - '0') +
                           3*(code.charAt(1) - '0') +
                             (code.charAt(2) - '0') +
                           3*(code.charAt(3) - '0') +
                             (code.charAt(4) - '0') +
                           3*(code.charAt(5) - '0') +
                             (code.charAt(6) - '0') +
                           3*(code.charAt(7) - '0') +
                             (code.charAt(8) - '0') +
                           3*(code.charAt(9) - '0') +
                             (code.charAt(10) - '0') +
                           3*(code.charAt(11) - '0'))%10) % 10;
        return (check == (code.charAt(12) - '0'));
    }

    /**
     * Determines whether the code is valid.
     *   @return true if a valid code, else false
     */
    public boolean isValid() {
        return this.isValid;
    }

    /**
     * Converts the ISBN13 object into a String.
     *   @return the String representation (including dashes)
     */
    public String toString() {
        return this.rawCode;
    }

    /**
     * Compares this ISBN13 object with another.
     * @param other the code to compare with
     * @return true if the current code comes before the other lexicographically,
     *         else false (note that dashes are ignored in the comparison) 
     */
    public int compareTo(CheckISBN13 other) {
        return this.strippedCode.compareTo(other.strippedCode);
    }

    /**
     * Tests equality of this ISBN13 object with another Object.
     * @param obj the object to be compared with
     * @return true if consist of the same sequence of digits (ignoring dashes),
     *         else false
     */
    public boolean equals(Object obj) {
        return this.strippedCode.equals(((CheckISBN13)obj).strippedCode);
    }


/*
    public static void main(String[] args) {
        ISBN13 code1 = new ISBN13("?78-03-2135-828-8");
        System.out.println(code1 + " " + code1.isValid());
        ISBN13 code2 = new ISBN13("9-7-80321-358288");
        System.out.println(code2 + " " + code2.isValid());
        ISBN13 code3 = new ISBN13("9-7-80A21-358288");
        System.out.println(code3 + " " + code3.isValid());
        System.out.println(code1.equals(code2));
    }

*/
}
