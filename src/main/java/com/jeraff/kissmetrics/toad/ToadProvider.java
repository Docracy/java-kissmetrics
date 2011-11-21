package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.toad.Toad;

public interface ToadProvider {
    Toad getToad();
    String getDefaultKissClientId();
}
