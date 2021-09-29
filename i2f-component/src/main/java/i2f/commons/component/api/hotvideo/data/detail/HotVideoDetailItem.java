package i2f.commons.component.api.hotvideo.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotVideoDetailItem {
    private String title;
    private String share_url;
    private String author;
    private String item_cover;
    private long hot_value;
    private String hot_words;
    private long play_count;
    private long digg_count;
    private long comment_count;
}
