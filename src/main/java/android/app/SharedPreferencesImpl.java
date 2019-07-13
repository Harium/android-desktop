package android.app;

import android.annotation.Nullable;
import android.content.SharedPreferences;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesImpl implements SharedPreferences {

    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";
    public static final String TAG_BOOLEAN = "boolean";
    public static final String TAG_FLOAT = "float";
    public static final String TAG_INT = "int";
    public static final String TAG_LONG = "long";
    public static final String TAG_STRING = "string";
    public static final String TAG_SET = "set";

    private final String name;
    private Map<String, Object> mMap = new HashMap<String, Object>();
    private Throwable mThrowable;

    private final Object mLock = new Object();
    private boolean mLoaded = false;

    public SharedPreferencesImpl(String preferences) {
        this.name = preferences;
        startLoadFromDisk();
    }

    public boolean getBoolean(String key, boolean defValue) {
        synchronized (mLock) {
            awaitLoadedLocked();
            Boolean v = (Boolean) mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public float getFloat(String key, float defValue) {
        synchronized (mLock) {
            awaitLoadedLocked();
            Float v = (Float) mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    @Override
    public int getInt(String key, int defValue) {
        synchronized (mLock) {
            awaitLoadedLocked();
            Integer v = (Integer) mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public long getLong(String key, long defValue) {
        synchronized (mLock) {
            awaitLoadedLocked();
            Long v = (Long) mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public String getString(String key, @Nullable String defValue) {
        synchronized (mLock) {
            awaitLoadedLocked();
            String v = (String) mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    @Override
    @Nullable
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        synchronized (mLock) {
            awaitLoadedLocked();
            Set<String> v = (Set<String>) mMap.get(key);
            return v != null ? v : defValues;
        }
    }

    @Override
    public boolean contains(String key) {
        synchronized (mLock) {
            awaitLoadedLocked();
            return mMap.containsKey(key);
        }
    }

    @Override
    public Map<String, ?> getAll() {
        synchronized (mLock) {
            awaitLoadedLocked();
            //noinspection unchecked
            return new HashMap<String, Object>(mMap);
        }
    }

    private void startLoadFromDisk() {
        synchronized (mLock) {
            mLoaded = false;
        }
        new Thread("SharedPreferencesImpl-load") {
            public void run() {
                loadFromDisk();
            }
        }.start();
    }

    private void awaitLoadedLocked() {
        while (!mLoaded) {
            try {
                mLock.wait();
            } catch (InterruptedException unused) {
            }
        }
        if (mThrowable != null) {
            throw new IllegalStateException(mThrowable);
        }
    }

    private void loadFromDisk() {
        synchronized (mLock) {
            if (mLoaded) {
                return;
            }
        }

        Map<String, Object> map = null;
        Throwable thrown = null;

        synchronized (mLock) {
            mLoaded = true;

            try {
                map = loadXML();
            } catch (Exception e) {
                thrown = e;
            }

            mThrowable = thrown;

            // It's important that we always signal waiters, even if we'll make
            // them fail with an exception. The try-finally is pretty wide, but
            // better safe than sorry.
            try {
                if (thrown == null) {
                    if (map != null) {
                        mMap = map;
                    } else {
                        mMap = new HashMap<>();
                    }
                }
                // In case of a thrown exception, we retain the old map. That allows
                // any open editors to commit and store updates.
            } catch (Throwable t) {
                mThrowable = t;
            } finally {
                mLock.notifyAll();
            }
        }
    }

    private Map<String, Object> loadXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        String path = getFilePath();
        String input = path + name + ".xml";

        Document document = builder.parse(new File(input));

        Element root = document.getDocumentElement();
        NodeList nList = root.getChildNodes();

        Map<String, Object> map = new HashMap<>();

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element valueNode = (Element) node;
                String tag = valueNode.getTagName();
                String key = valueNode.getAttribute(ATTR_NAME);

                if (TAG_BOOLEAN.equals(tag)) {
                    String value = valueNode.getAttribute(ATTR_VALUE);
                    map.put(key, Boolean.valueOf(value));
                } else if (TAG_FLOAT.equals(tag)) {
                    String value = valueNode.getAttribute(ATTR_VALUE);
                    map.put(key, Float.valueOf(value));
                } else if (TAG_INT.equals(tag)) {
                    String value = valueNode.getAttribute(ATTR_VALUE);
                    map.put(key, Integer.valueOf(value));
                } else if (TAG_LONG.equals(tag)) {
                    String value = valueNode.getAttribute(ATTR_VALUE);
                    map.put(key, Long.valueOf(value));
                } else if (TAG_STRING.equals(tag)) {
                    String value = valueNode.getTextContent();
                    map.put(key, value);
                } else if (TAG_SET.equals(tag)) {
                    Set<String> set = new HashSet<>();
                    NodeList list = valueNode.getChildNodes();
                    for (int i = 0; i < list.getLength(); i++) {
                        Node item = list.item(i);
                        if (item.getNodeName().equals(TAG_STRING)) {
                            set.add(item.getTextContent());
                        }
                    }
                    map.put(key, set);
                }
            }
        }

        return map;
    }

    public Editor edit() {
        return new EditorImpl();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    public class EditorImpl implements Editor {

        Map<String, Object> values = new HashMap<String, Object>();

        @Override
        public Editor remove(String key) {
            values.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            values.clear();
            return this;
        }

        public Editor putBoolean(String key, boolean value) {
            values.put(key, value);
            return this;
        }

        public Editor putFloat(String key, float value) {
            values.put(key, value);
            return this;
        }

        public Editor putInt(String key, int value) {
            values.put(key, value);
            return this;
        }

        public Editor putLong(String key, long value) {
            values.put(key, value);
            return this;
        }

        public Editor putString(String key, String value) {
            values.put(key, value);
            return this;
        }

        public Editor putStringSet(String name, Set<String> value) {
            values.put(name, value);
            return this;
        }

        public void apply() {
            new Thread(new Runnable() {
                public void run() {
                    commit();
                }
            }).start();
        }

        public boolean commit() {

            try {
                DOMSource source = buildSource(values);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                String path = getFilePath();

                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File output = new File(path + name + ".xml");

                StreamResult file = new StreamResult(output);
                transformer.transform(source, file);
            } catch (TransformerException | ParserConfigurationException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    private String getFilePath() {
        String root = getRoot();
        String folder = getPackageName();
        return root + "/data/data/" + folder + "shared_prefs/";
    }

    private String getRoot() {
        return System.getProperty("user.dir");
    }

    private String getPackageName() {
        String pkg = System.getProperty("sun.java.command");
        int lastDot = pkg.lastIndexOf('.');
        pkg = pkg.substring(0, lastDot);
        pkg += File.separator;
        return pkg;
    }

    private DOMSource buildSource(Map<String, Object> values) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        doc.setXmlStandalone(true);

        Element root = doc.createElement("map");
        doc.appendChild(root);
        exportData(doc, root, values);

        return new DOMSource(doc);
    }

    private static void exportData(Document doc, Element root, Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Boolean) {
                exportBoolean(doc, root, key, value);
            } else if (value instanceof Integer) {
                exportInt(doc, root, key, value);
            } else if (value instanceof Float) {
                exportFloat(doc, root, key, value);
            } else if (value instanceof Long) {
                exportLong(doc, root, key, value);
            } else if (value instanceof String) {
                exportString(doc, root, key, value);
            } else if (value instanceof Set) {
                exportStringSet(doc, root, key, value);
            }
        }
    }

    private static void exportBoolean(Document doc, Element root, String key, Object value) {
        exportValue(doc, root, TAG_BOOLEAN, key, value);
    }

    private static void exportFloat(Document doc, Element root, String key, Object value) {
        exportValue(doc, root, TAG_FLOAT, key, value);
    }

    private static void exportLong(Document doc, Element root, String key, Object value) {
        exportValue(doc, root, TAG_LONG, key, value);
    }

    private static void exportInt(Document doc, Element root, String key, Object value) {
        exportValue(doc, root, TAG_INT, key, value);
    }

    private static void exportValue(Document doc, Element root, String type, String key,
                                    Object value) {
        Element node = doc.createElement(type);
        node.setAttribute(ATTR_NAME, key);
        node.setAttribute(ATTR_VALUE, String.valueOf(value));
        root.appendChild(node);
    }

    private static void exportString(Document doc, Element root, String key, Object value) {
        Element node = doc.createElement(TAG_STRING);
        node.setAttribute(ATTR_NAME, key);
        node.setTextContent(String.valueOf(value));
        root.appendChild(node);
    }

    private static void exportStringSet(Document doc, Element root, String key, Object value) {
        Set<String> set = (Set<String>) value;

        Element node = doc.createElement(TAG_SET);
        node.setAttribute(ATTR_NAME, key);
        root.appendChild(node);

        for (String item : set) {
            Element itemNode = doc.createElement(TAG_STRING);
            itemNode.setTextContent(item);
            node.appendChild(itemNode);
        }
    }
}
