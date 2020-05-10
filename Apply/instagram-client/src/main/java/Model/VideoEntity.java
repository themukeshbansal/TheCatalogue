package Model;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
public class VideoEntity {
    private final String name;
    private final String url;
    private final String destinationFolder;
    private final Suffix suffix;
//    private final Date dateTimeAdded;
}
