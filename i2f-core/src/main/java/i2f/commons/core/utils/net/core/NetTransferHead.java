package i2f.commons.core.utils.net.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ltb
 * @date 2022/2/20 15:36
 * @desc
 */
@Data
@NoArgsConstructor
public class NetTransferHead implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;
    private String mimeType;
    private long contentLength;

}
