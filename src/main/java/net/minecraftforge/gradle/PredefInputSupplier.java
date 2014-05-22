package net.minecraftforge.gradle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraftforge.srg2source.util.io.InputSupplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PredefInputSupplier implements InputSupplier
{
    private final Map<String, byte[]> fileMap = Maps.newHashMap();
    private final Map<String, File>   rootMap = Maps.newHashMap();

    @Override
    public void close() throws IOException
    {
        // uh.. no?
    }

    @Override
    public String getRoot(String resource)
    {
        return rootMap.get(sanitize(resource)).getAbsolutePath();
    }

    @Override
    public InputStream getInput(String relPath)
    {
        return new ByteArrayInputStream(fileMap.get(relPath));
    }

    @Override
    public List<String> gatherAll(String endFilter)
    {
        LinkedList<String> out = Lists.newLinkedList();
        for (String s : fileMap.keySet())
        {
            if (s.endsWith(endFilter))
            {
                out.add(s);
            }
        }

        return out;
    }

    public void addFile(String path, File root, byte[] data) throws IOException
    {
        root = root.getCanonicalFile();
        fileMap.put(path, data);
        rootMap.put(path, root);
    }

    private String sanitize(String in)
    {
        return in.replace('\\', '/');
    }

    public boolean isEmpty()
    {
        return fileMap.isEmpty() && rootMap.isEmpty();
    }
}