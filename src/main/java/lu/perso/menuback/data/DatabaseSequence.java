package lu.perso.menuback.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
public record DatabaseSequence (@Id String id, long seq) {
}