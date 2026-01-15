package com.xyrisdev.mist.extension.render.common;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

public interface RenderHandler {

    void handle(@NotNull ChatContext ctx);
}