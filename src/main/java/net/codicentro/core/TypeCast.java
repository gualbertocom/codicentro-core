/*
 * Author: Alexander Villalobos Yadró
 * E-Mail: avyadro@yahoo.com.mx
 * Created on May 19, 2008, 10:27:26 AM
 * Place: Querétaro, Querétaro, México.
 * Company: Codicentro©
 * Web: http://www.codicentro.net
 * Class Name: TypeCast.java
 * Purpose:
 * Revisions:
 * Ver        Date               Author                                      Description
 * ---------  ---------------  -----------------------------------  ------------------------------------
 * 1.0        May 19, 2008           Alexander Villalobos Yadró           1. New class.
 **/
package net.codicentro.core;

//import net.codicentro.model.Column;
//import net.codicentro.model.Table;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TypeCast {

    public static Object ifNull(Object o, Object r) {
        return o == null ? r : o;
    }

    public static boolean isBlank(String s) {
        return (s == null || s.trim().equals(""));
    }

    /**
     * Remplaza el valor s por r en caso de que se cumpla la condicion.
     *
     * @param s, Valor
     * @param r, Remplazo
     * @return
     */
    public static String rplNullOrEmpty(String s, String r) {
        if (isBlank(s)) {
            return r;
        } else {
            return s;
        }
    }

    public static boolean isNullOrEmpty(String s, String v) {
        s = (s != null) ? s.replaceAll(v, "") : s;
        return isBlank(s);
    }

    /**
     *
     * @param s
     * @param r
     * @param size
     * @param at
     * @return
     */
    public static String CompleteString(String s, String r, int size, Types.AlignmentType at) {
        int l = ((size < s.length()) ? s.length() : size) - s.length();
        for (int i = 0; i < l; i++) {
            switch (at) {
                case LEFT:
                    s = r.concat(s);
                    break;
                case RIGHT:
                    s = s.concat(r);
                    break;
            }
        }
        return s;
    }

    public static String cuotes(String s) {
        if (s == null) {
            return null;
        } else {
            return s.replaceAll("\"", "\\\\\"");
        }
    }

    public static String CompleteString(int ascii, String r, int size, Types.AlignmentType at) {
        return CompleteString(toString(ascii), r, size, at);
    }

    public static <T> void clone(Object o) {
        //  Object newObject = (String)super
    }

    public static boolean toBoolean(Object obj) {
        try {
            String s = toString(obj);
            if (s != null) {
                s = s.trim().toUpperCase();
            } else {
                s = "N";
            }
            return !s.equals("N") && !s.equals("NO") && !s.equals("FALSE") && !s.equals("F") && !((ifNumber(s)) && (toInt(s) == 0)) && !s.equals("OFF");
        } catch (CDCException ex) {
            return false;
        }
    }

    public static boolean ifNumber(Object o) throws CDCException {
        try {
            Double.parseDouble(toString(o));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static int toInt(Object obj) throws CDCException {
        return toInt(toString(obj));
    }

    public static String OnlyWords(String s) throws CDCException {
        String result = "";
        try {
            result = s.replaceAll("[^a-z]||[^A-Z]", "");
        } catch (Exception ex) {
            throw new CDCException(ex);
        }
        return result;
    }

    public static Integer OnlyNumber(String s) throws CDCException {
        Integer result = 0;
        try {
            result = Integer.valueOf(s.replaceAll("[^0-9]", ""));
        } catch (Exception ex) {
            throw new CDCException(ex);
        }
        return result;
    }

    public static int toInt(String str) throws CDCException {
        int r = 0;
        try {
            r = (((str == null) || (str.trim().equals(""))) ? 0 : Integer.parseInt(str.trim()));
        } catch (Exception ex) {
            throw new CDCException(ex);
        }
        return r;
    }

    public static Integer toInteger(int pInt) {
        return pInt;
    }

    public static Integer toInteger(String str) {
        BigDecimal v = toNumeric(str);
        return v == null ? null : v.intValue();
    }

    public static Double toDouble(Object o) {
        BigDecimal v = toNumeric(o);
        return v == null ? null : v.doubleValue();
    }

    /**
     *
     * @param o
     * @return
     */
    public static BigInteger toBigInteger(Object o) {
        BigDecimal v = toNumeric(o);
        return v == null ? null : BigInteger.valueOf(v.longValue());
    }

    /**
     *
     * @param d, Date
     * @param f, Format ex. yyyyMMdd
     * @return
     * @throws CDCException
     */
    public static BigInteger toBigInteger(Date d, String f) throws CDCException {
        return toBigInteger(toString(d, f));
    }

    /**
     *
     * @param o
     * @return
     */
    public static BigDecimal toBigDecimal(Object o) {
        return toNumeric(o);
    }

    private static BigDecimal toNumeric(Object o) {
        try {
            String v = toString(o);
            if (isBlank(v)) {
                return null;
            } else {
                v = v.replaceFirst("^0*", "");
                return new BigDecimal(isBlank(v) ? "0" : v);
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Long toLong(Object o) {
        BigDecimal v = toNumeric(o);
        return v == null ? null : v.longValue();
    }

    /**
     * Converts Object Type to String Type
     *
     * @param o
     * @return
     */
    public static String toString(Object o) {
        try {
            return (o == null) ? null : String.valueOf(o);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String toString(int i) {
        return String.valueOf(i);
    }

    /**
     * Converts Object Type to String Type and Replace value r if value is null
     *
     * @param o
     * @param r
     * @return
     */
    public static String toString(Object o, String r) {
        try {
            return (toString(o) == null) ? r : toString(o);
        } catch (Exception ex) {
            return r;
        }
    }
    /*
     * public static Object NVL(Object o) { return ((o == null) ? "NULL" : o); }
     */

    public static Object NVL(Object o, Object r) {
        return (((o == null) || (o.equals("") || (o.equals("NULL")))) ? r : o);
    }

    public static String NVL(String o, String r) {
        return (((o == null) || (o.equals("") || (o.equals("NULL")))) ? r : o);
    }

    public static String NVL(String o, String r, boolean trim) {
        o = (o == null) ? "" : o;
        r = (r == null) ? "" : o;
        o = (trim) ? o.trim() : o;
        r = (trim) ? r.trim() : r;
        return (((o.equals("") || (o.equals("NULL")))) ? r : o);
    }

    public static Integer toInteger(Object o) {
        BigDecimal v = toNumeric(o);
        return v == null ? null : v.intValue();
    }

    /**
     *
     * @param o
     * @return
     */
    public static Short toShort(Object o) {
        BigDecimal v = toNumeric(o);
        return v == null ? null : v.shortValue();
    }

    /**
     * Decodes a String into a Short. Accepts decimal, hexadecimal, and octal
     * numbers.
     *
     * @param o
     * @return
     * @throws CDCException
     */
    public static Short toShortD(String o) throws CDCException {
        try {
            return Short.decode(o);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Float toFloat(Object o) {
        try {
            return Float.valueOf(toString(o));
        } catch (Exception ex) {
            return null;
        }
    }

    public static String EMPTY(String o, String r) {
        return (((o == null) || (o.equals(""))) ? r : o);
    }

    public static String toBlanc(String o) {
        return (((o == null) || (o.equals("-1"))) ? "" : o);
    }

    public static BigDecimal toBigDecimalOrNull(Object o) throws CDCException {
        if ((o == null) || (o.equals(""))) {
            return null;
        }
        return new BigDecimal(toString(o));
    }

    /**
     *
     * @param d, Date
     * @param f, Date format
     * @return
     */
    public static String toString(Date d, String f) {
        return toString(d, f, Locale.getDefault());
    }

    public static String toString(Date d, String f, String locale) {
        return toString(d, f, new Locale(locale));
    }

    public static String toString(Date d, String f, Locale locale) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(f.trim(), locale);
            df.setLenient(false); // Force read format date into param f
            return df.format(d);
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar toCalendar(int date) {
        int day = date % 100;
        int month = date / 100 % 100 - 1;
        int year = date / 10000;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal;
    }

    /**
     *
     * @param cal
     * @return
     */
    public static int toDate(Calendar cal) {
        int day = cal.get(5);
        int month = cal.get(2) + 1;
        int year = cal.get(1);
        return (year * 10000 + month * 100 + day);
    }

    /**
     *
     * @param o
     * @param f
     * @return
     */
    public static Date toDate(Object o, String f) {
        return toDate(o, f, false);
    }

    /**
     *
     * @param o
     * @param f
     * @param e, When e is true and no format convert then throw exception else
     * return null when is not format convert.
     * @return
     */
    public static Date toDate(Object o, String f, boolean e) {
        try {
            if (isBlank(toString(o))) {
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat(f.trim());
            df.setLenient(false); // Force read format date into param f
            return df.parse(toString(o));
        } catch (Exception ex) {
            if (e) {
                throw new RuntimeException("Invalid convert object to date.");
            } else {
                return null;
            }
        }
    }

    /**
     *
     * @param o, Date
     * @param fi, Format Input
     * @param fo, Format Output
     * @return
     */
    public static Date toDate(Object o, String fi, String fo) {
        try {
            return toDate(toDate(o, fi), fo);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date toDate(Object o, String f, Locale locale) {
        try {
            if (isBlank(toString(o))) {
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat(f.trim(), locale);
            df.setLenient(false); // Force read format date into param f
            return df.parse(toString(o));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     *
     * @param o
     * @param f
     * @return
     * @throws CDCException
     */
    public static java.sql.Date toSqlDate(Object o, String f) throws CDCException {
        return new java.sql.Date(toDate(o, f).getTime());
    }

    /**
     *
     * @param s
     * @return
     */
    public static String[] toArray(String s) {
        return toArray(s, true);
    }

    public static String[] toArray(String s, boolean allowNull) {
        return toArray(s, ",", allowNull);
    }

    public static String[] toArray(String s, String d, boolean allowNull) {
        List<String> data = new ArrayList<String>();
        String[] split = s.split(d);
        for (String value : split) {
            if (allowNull || (!allowNull && !isBlank(value))) {
                data.add(value);
            }
        }
        String[] rs = new String[data.size()];
        data.toArray(rs);
        return rs;
    }

    /**
     *
     * @param s Data
     * @param r - the delimiters of row.
     * @param e - the delimiters of element.
     * @param o - the delimiters of key and value.
     * @return
     * @throws CDCException
     */
    public static Object[] toArray(String s, String r, String e, String o) throws CDCException {
        StringTokenizer idx = new StringTokenizer(s, r);
        ArrayList rs = (idx.hasMoreTokens()) ? new ArrayList() : null;
        String k = null;//Key
        String v = null;// Value
        while (idx.hasMoreTokens()) {
            HashMap hm = new HashMap();
            StringTokenizer cm = new StringTokenizer(idx.nextToken(), e);
            while (cm.hasMoreTokens()) {
                StringTokenizer tp = new StringTokenizer(cm.nextToken(), o);
                if (tp.hasMoreTokens()) {
                    k = tp.nextToken();
                    v = (tp.hasMoreTokens()) ? tp.nextToken() : "";
                } else {
                    throw new CDCException("lng.msg.error.malformedarray('" + s + "')");
                }
                hm.put(k, v);
            }
            rs.add(hm);
        }
        return rs.toArray();
    }

    /**
     *
     * @param c
     * @return
     */
    public static String toString(char[] c) {
        String result = "";
        for (int i = 0; i < c.length; i++) {
            result += c[i];
        }
        return result;
    }

    /**
     *
     * @param s
     * @param separator
     * @return
     */
    public static String toString(String[] s, String separator) {
        if (s == null || s.length < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(s[i]);
        }
        return sb.toString();
    }

    public static String toString(String[] s, String separator, String begin, String end) {
        if (s == null || s.length < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(begin).append(s[i]).append(end);
        }
        return sb.toString();
    }

    public static <T> String toString(List<T> list) throws CDCException {
        String rs = "";
        for (T o : list) {
            rs += (isBlank(rs)) ? toString(o) : "," + toString(o);
        }
        return rs;
    }

    public static String toString(byte[] b) {
        //StringList sb = new StringList();
        String result = "";
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                result += "0";
            }
            result += Integer.toHexString(v);
        }
        return result;
    }

    public static String toFirtLowerCase(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static String toFirtUpperCase(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String toOctal(String v) {
        v = v.replaceAll("/\r\n/g", "\n");
        String rs = "";
        char c;
        for (int n = 0; n < v.length(); n++) {
            c = v.charAt(n);
            if (c < 128) {
                rs += String.valueOf(c);
            } else {
                // String.valueOf(c).
                //      rs += "\\" + String.valueOf(c).;
            }
        }

        return null;
    }

    /**
     * Get Object by name, used reflections for find method
     *
     * @param o Instance of the class represented by this object
     * @param n Method name
     * @return
     * @throws net.codicentro.core.CDCException
     */
    public static Object GN(Object o, String n) throws CDCException {
        if (o == null) {
            throw new CDCException("core.typecast.gn.msg.error.objectisnull(\"" + n + "\")");
        }
        Method m = getMethod(o.getClass(), n);
        if (m != null) {
            return invoke(m, o, null);
        } else {
            throw new CDCException("core.typecast.gn.msg.error.methodnotfound(\"" + n + "\n)");
        }
    }

    /**
     * Get Field Value by Name, used reflections for find field
     *
     * @param o Instance of the class represented by this object
     * @param n Field name
     * @return
     * @throws CDCException
     */
    public static Object GF(Object o, String n) throws CDCException {
        if (o == null) {
            throw new CDCException("core.typecast.gf.msg.error.objectisnull(\"" + n + "\")");
        }
        try {
            Field f = o.getClass().getField(n);
            return f.get(o);
        } catch (IllegalAccessException ex) {
            throw new CDCException("core.typecast.gf.msg.error.IllegalAccessException(\"" + n + "\")");
        } catch (IllegalArgumentException ex) {
            throw new CDCException("core.typecast.gf.msg.error.IllegalArgumentException(\"" + n + "\")");
        } catch (NoSuchFieldException ex) {
            throw new CDCException("core.typecast.gf.msg.error.NoSuchFieldException(\"" + n + "\")");
        } catch (SecurityException ex) {
            throw new CDCException("core.typecast.gf.msg.error.SecurityException(\"" + n + "\")");
        }
    }

    /**
     *
     * @param className
     * @param n
     * @return
     * @throws CDCException
     */
    public static Object GF(String className, String n) throws CDCException {
        if (className == null) {
            throw new CDCException("core.typecast.gf.msg.error.objectisnull(\"" + n + "\")");
        }
        try {
            return GF(Class.forName(className).newInstance(), n);
        } catch (ClassNotFoundException ex) {
            throw new CDCException("core.typecast.gf.msg.error.ClassNotFoundException(\"" + className + "\")");
        } catch (InstantiationException ex) {
            throw new CDCException("core.typecast.gf.msg.error.InstantiationException(\"" + className + "\")");
        } catch (IllegalAccessException ex) {
            throw new CDCException("core.typecast.gf.msg.error.IllegalAccessException(\"" + className + "\")");
        }
    }

    /**
     * Set Object by name, used reflections for find method
     *
     * @param o, Object
     * @param n, Method name
     * @param value
     * @throws CDCException
     */
    public static void SN(Object o, String n, Object value) throws CDCException {
        Method m = getMethod(o.getClass(), n);
        if (m != null) {
            invoke(m, o, value);
        } else {
            throw new CDCException("cliser.msg.error.remove.jointable.methodcannotbefound");
        }
    }

    /**
     *
     * @param c, Class name
     * @param n, Public method name
     * @param parameterTypes,
     * @return
     * @throws net.codicentro.core.CDCException
     */
    public static Method getMethod(Class c, String n, Class<?>... parameterTypes) throws CDCException {
        try {
            if (parameterTypes == null) {
                return c.getMethod(n);
            } else {
                return c.getMethod(n, parameterTypes);
            }
        } catch (NoSuchMethodException ex) {
            throw new CDCException(ex);
        } catch (SecurityException ex) {
            throw new CDCException(ex);
        }
    }

    /**
     * Get Field Class
     *
     * @param c
     * @param n
     * @return
     * @throws CDCException
     */
    public static Field getField(Class c, String n) throws CDCException {
        try {
            return c.getField(n);
        } catch (NoSuchFieldException ex) {
            throw new CDCException(ex);
        } catch (SecurityException ex) {
            throw new CDCException(ex);
        }
    }

    /**
     *
     * @param m
     * @param o
     * @param p, Parameters, set null for optional param
     * @return
     * @throws net.codicentro.core.CDCException
     * @deprecated
     */
    public static Object invoke(Method m, Object o, Object p) throws CDCException {
        try {
            if (p == null) {
                return m.invoke(o);
            } else {
                return m.invoke(o, p);
            }
        } catch (IllegalAccessException ex) {
            throw new CDCException(ex);
        } catch (IllegalArgumentException ex) {
            throw new CDCException(ex);
        } catch (InvocationTargetException ex) {
            throw new CDCException(ex);
        }
    }

    /**
     *
     * @param o Object
     * @param m Method name
     * @param types Class type params
     * @param args Params
     * @return
     * @throws CDCException
     */
    public static Object invoke(Object o, String m, Class[] types, Object[] args) throws CDCException {
        try {
            if ((types == null) && (args == null)) {
                return o.getClass().getMethod(m).invoke(o);
            }
            if (types.length != args.length) {
                throw new Exception("The number params types not match the arguments.");
            }
            /**
             * Verify types args
             */
            for (int idx = 0; idx < types.length; idx++) {
                if ((args[idx] != null) && (types[idx] != args[idx].getClass())) {
                    throw new Exception("Params types not match arguments type in position" + idx + ".");
                }
            }
            return o.getClass().getMethod(m, types).invoke(o, args);
        } catch (Exception ex) {
            throw new CDCException(ex);
        }
    }

    public static char toChar(Object c) {
        return toCharacter(c);
    }

    public static java.lang.Character toCharacter(Object c) {
        String rs = toString(c);
        return isBlank(rs) ? null : rs.charAt(0);
    }

    public static String toString(BigDecimal n, String f) {
        try {
            DecimalFormat df = new DecimalFormat(f.trim());
            return df.format(n);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toString(Double n, String f) {
        try {
            DecimalFormat df = new DecimalFormat(f.trim());
            return df.format(n);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param n, Value
     * @param f, Format
     * @param r, Replace value when is null
     * @return
     */
    public static String toString(BigDecimal n, String f, String r) {
        String rs = toString(n, f);
        if (isBlank(rs)) {
            return r;
        } else {
            return rs;
        }
    }

    /**
     * @param n, value numeric
     * @param f, numeric format
     * @param d, default value when n is null
     * @return
     */
    public static String toString(BigDecimal n, String f, BigDecimal d) {
        try {
            String rs = toString(n, f);
            return ((rs == null) ? toString(d, f) : rs);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNumber(Object o) {
        if (isBlank(toString(o))) {
            return false;
        }
        return toString(o).matches("[-+]?\\d*\\.?\\d+");
    }

    public static String toSoutFormat(String s) {
        String sf = "";
        for (int i = 0; i < s.length() + 4; i++) {
            sf += "*";
        }
        return sf + "\n" + "* " + s + " *\n" + sf + "\n";
    }

    /**
     *
     * @param v
     * @param size
     * @return
     */
    public static String repeat(String v, int size) {
        String rs = "";
        for (int i = 0; i < size; i++) {
            rs += v;
        }
        return rs;
    }

    /**
     * Customized format
     *
     * @param prefix
     * @param complete
     * @param count
     * @param v
     * @return
     */
    public static String CF(String prefix, String complete, int count, Object v) {
        return prefix + repeat(complete, (count - toString(v).length())) + v;
    }

    /**
     *
     * @param os
     * @return
     */
    public static InputStream toInputStream(OutputStream os) {
        return new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
    }

    /**
     *
     * @param file
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static InputStream toInputStream(File file) throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        is.close();
        return is;
    }

    public static InputStream toInputStream(String str) throws FileNotFoundException {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static byte[] toBytes(File file) throws IOException {
        ByteArrayOutputStream baos = toByteArrayOutputStream(file);
        if (baos != null) {
            return baos.toByteArray();
        } else {
            return null;
        }
    }

    public static ByteArrayOutputStream toByteArrayOutputStream(File file) throws IOException {
        return toByteArrayOutputStream(new FileInputStream(file));
    }

    public static ByteArrayOutputStream toByteArrayOutputStream(byte[] bs) throws IOException {
        return toByteArrayOutputStream(toInputStream(bs));
    }

    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream ios) throws IOException {
        ByteArrayOutputStream baos = null;
        try {
            byte[] buffer = new byte[4096];
            baos = new ByteArrayOutputStream();
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                // swallow, since not that important
            }
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                // swallow, since not that important
            }
        }
        return baos;
    }

    /**
     * Gets the exception stack trace as a string.
     *
     * @param ex
     * @return
     */
    public static String toString(Throwable ex) {
        if (ex == null) {
            return "";
        }
        StringWriter str = new StringWriter();
        PrintWriter writer = new PrintWriter(str);
        try {
            ex.printStackTrace(writer);
            return str.getBuffer().toString();
        } finally {
            try {
                str.close();
                writer.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String toString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the Reader.read(char[]
         * buffer) method. We iterate until the Reader return -1 which means
         * there's no more data to read. We use the StringWriter class to
         * produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static Object round(Object value, int places) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(places);
        return nf.format(value);
    }

    public static <TEntity> TEntity cast(Object value, Class<TEntity> clazz) {
        return clazz.cast(value);
    }

    public static String[] join(String[] array1, String[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 != null && array2 == null) {
            return array1;
        }
        if (array1 == null && array2 != null) {
            return array2;
        }
        List<String> list = new ArrayList<String>(Arrays.asList(array1));
        list.addAll(Arrays.asList(array2));
        return list.toArray(new String[0]);
    }

    public static Object[] join(Object[] array1, Object[] array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 != null && array2 == null) {
            return array1;
        }
        if (array1 == null && array2 != null) {
            return array2;
        }
        List<Object> list = new ArrayList<Object>(Arrays.asList(array1));
        list.addAll(Arrays.asList(array2));
        return list.toArray(new Object[0]);
    }

    /**
     * If type is primitive then convert to object else return equal type.
     *
     * @param type
     * @return
     */
    public static Class<?> toObject(Class<?> type) {
        if (type.getName().equals("char")) {
            return Character.class;
        } else if (type.getName().equals("byte")) {
            return Byte.class;
        } else if (type.getName().equals("short")) {
            return Short.class;
        } else if (type.getName().equals("int")) {
            return Integer.class;
        } else if (type.getName().equals("long")) {
            return Long.class;
        } else if (type.getName().equals("float")) {
            return Float.class;
        } else if (type.getName().equals("double")) {
            return Double.class;
        } else if (type.getName().equals("boolean")) {
            return Boolean.class;
        } else if (type.getName().equals("void")) {
            return Void.class;
        } else {
            return type;
        }
    }

    public static Object cast(Class<?> type, Object value) {
        if (type.getSimpleName().equals("BigInteger")) {
            return toBigInteger(value);
        } else if (type.getSimpleName().equals("BigDecimal")) {
            return toBigDecimal(value);
        } else if (type.getSimpleName().equals("Long")) {
            return toLong(value);
        } else {
            return toString(value);
        }
    }

    public static byte[] toBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;
        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        }
        return buf;
    }

    public static InputStream toInputStream(byte[] bs) {
        return new ByteArrayInputStream(bs);
    }

    public static String toString(File textfile) throws FileNotFoundException, IOException {
        StringBuilder rs = new StringBuilder();
        FileReader fr = new FileReader(textfile);
        BufferedReader br = new BufferedReader(fr);
        String ln;
        while ((ln = br.readLine()) != null) {
            rs.append(ln);
        }
        fr.close();
        return rs.toString();
    }

    public static String encodeUTF8(String s) {
        try {
            return isBlank(s) ? null : new String(s.getBytes("ISO-8859-1"), Charset.forName("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static Long[] toLongArray(String[] numbers) {
        List<Long> rs = new ArrayList<Long>();
        for (String number : numbers) {
            rs.add(toLong(number));
        }
        return rs.toArray(new Long[]{});
    }

}
