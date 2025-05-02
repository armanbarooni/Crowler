package com.atlas.crawler.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CveQueueManager {
    public static final BlockingQueue<CVE> cveQueue = new LinkedBlockingQueue<>();
}
