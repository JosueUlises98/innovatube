package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeThumbnails {
    private String videoId;
    private String defaultUrl;
    private String mediumUrl;
    private String highUrl;
    private String standardUrl;
    private String maxresUrl;
    private int defaultWidth;
    private int defaultHeight;
    private int mediumWidth;
    private int mediumHeight;
    private int highWidth;
    private int highHeight;
    private int standardWidth;
    private int standardHeight;
    private int maxresWidth;
    private int maxresHeight;
} 