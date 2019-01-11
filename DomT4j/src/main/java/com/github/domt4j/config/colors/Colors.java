package com.github.domt4j.config.colors;

import cloud.jgo.utils.command.annotations.Configurable;

public interface Colors {
	public abstract Configurable getConfigByTarget(String target);
}
