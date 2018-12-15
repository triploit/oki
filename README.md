# OKI - Ohne Künstliche Intelligenz

Um OKI aufzusetzen, braucht er eine Datei, indem alle Einstellungen eingetragen werden.
Diese muss im JSON Format sein und kann so aussehen:

```json
{
  "master_identity": "<USER_ID>",
  "bot_token": "<BOT_TOKEN>",
  "brain_file": "brain.sav",
  "ban_file": "<NOCH_NICHT_IMPLEMENTIERT>",
  "blacklist_file": "blacklist.sav"
}
```

Folgendes haben diese zu bedeuten:

| Schlüssel | Bedeutung |
|-----|-----------|
| `master_identity` | User ID, die den Administrator für den Bot angibt. |
| `bot_token` | Der Bot Token für den Bot Account, er muss selber gehostet werden. |
| `brain_file` | Die Datei, in die alles gelernte reingespeichert werden soll. |
| `blacklist_file` | Die Datei, die alle verbotenen Wörter auflistet. |
| `ban_file` | (Noch nicht implementiert) |

## Was kann OKI?

OKI ist ein ChatBot, der dazu lernen kann. Jedoch nur auf stupide Frage-Antwort Weise (daher ohne künstliche Intelligenz). Versteht OKI einen Satz nicht, stellt er ihn erneut und erwarter eine Antwort darauf. Das wird dann gespeichert.
Beim nächsten mal kann OKI die Frage dann beantworten. Bis auf in den Antworten, die OKI fordert, muss in jeder Nachricht, die OKI ansprechen soll, ein "oki" darin vorkommen.

Beispiel:
```
User: Oki wie geht es dir?
OKI: wie geht es dir?
User: Mir geht es gut.

User: Wie geht es dir, oki?
OKI: Mir geht es gut.
```
OKI hat daher aus einem Satz, den er nicht konnte gelernt.

### Zufällige Antworten in OKI

Außerdem ist es mit OKI möglich, zufällige Antworten auf eine Frage antworten zu lassen.
Bei der Beispielfrage "oki würfeln", soll OKI eine Zahl von 1 bis 6 ausgeben.
Umgesetzt wird das so:

```
User: Oki würfeln
OKI: würfeln
User: 1~2~3~4~5~6

User: oki würfeln
OKI: 2
User: oki würfeln
OKI: 5
User: oki würfeln
OKI: 1
```

Das geschieht, da die Tilde (das hier: `~`) die Antworten an der Stelle aufteilt und einen zufälligen Teil zurückgibt.
Um eine Tilde zu schreiben und die Nachricht dort nicht aufzuteilen, kann man `\~` schreiben.

### Variablen in OKI

Weiterhin gibt es auch Variablen, wie zum Beispiel:

| Variable | Wert |
| ----- | --- |
| `$sender` | Der Absender der Nachricht. |
| `$user` | Alternative zu `$sender`  |
| `$time` | Aktuelle Zeit. Beispiel: 19:23  |
| `$master` | Enthält den Namen des Administrators, dessen ID in der Einstellungsdatei angegeben wurde. |

Diese kann man so einsetzen:

```
User: Oki wer bin ich?
OKI: wer bin ich?
User: Du bist $sender!

User: Oki wer bin ich?
OKI: Du bist User!
```

Möchte man eine Variable angeben, ohne dass sie gesetzt wird, muss man z.B. statt `$sender`, `$$sender` schreiben.

### Nachrichten nur an bestimmte Nutzer

Es gibt die Möglichkeiten, eine Nachricht nur an bestimmte Nutzer zu schreiben. Dafür gibt es eine JSON-Ähnliche Darstellungssprache, die man auch mit den Variablen kombinieren kann:

```
User: oki wer bin ich?
OKI: wer bin ich?
```
Der User antwortet auf die Frage von OKI mit:
```
!{c} (
    "$master": "Du bist hier der Administrator!";
    "<other>": "Du bist $sender.";
)
```

Wie man sich denken kann, passiert folgendes: Wenn der Administrator, der in der Einstellungsdatei angegeben wurde, fragt: `oki wer bin ich?`, antwortet OKI: `Du bist hier der Administrator!`. Frage aber ein anderer User die Frage, macht das `<other>`, dass alle anderen die Nachricht `Du bist $sender.` erhalten.

Ein weiteres Beispiel. Die Frage war: `oki, bin ich der echte?`
```
!{c} (
    "DerEchte": "Du bist wirklich der Echte!";
    "<other>": "Du bist nicht der Echte."
)
```

Der User mit dem Namen `DerEchte` erhält die Antwort `Du bist wirklich der Echte!`, alle anderen bekommen ein `Du bist nicht der Echte.` zurück.

Nun kann man auch hier mehrere User und Antworten angeben:
```
!{c} (
    "DerEchte" "$master": "Dich kenne ich!" "Hey :)";
    "<other>": "Dich kenne ich noch nicht." "Schön dich kennen zu lernen!";
)
```

Der Nutzer mit dem Namen `DerEchte` und der Administrator von OKI werden zufällig die Nachricht `Dich kenne ich!` oder `Hey :)` zurückbekommen. Alle anderen bekommen entweder die Nachricht `Dich kenne ich noch nicht.` oder `Schön dich kenne zu lernen!`.

## OKI hosten

Dazu muss einfach die Einstellungsdatei erstellt werden, außerdem noch die Brain-Datei und die Blacklist-Datei.
Nachdem mit Maven eine jar-Datei erstellt wurde (oder bei den Releases heruntergeladen wurde), muss OKI einfach nur die Einstellungsdatei als Parameter erhalten und startet dann:

```bash
java -jar oki.jar settings.json
```

### Die Brain Datei bearbeiten

Die Datei sollte nur bearbeitet werden, wenn eine unerwünschte Eingabe getätigt wurde.
Die gewählten Zeilen müssen dann gelöscht werden. Man findet diese, indem man erstmal die Frage wissen muss, die mit der unerwünschten Antwort zusammenhängt. Wenn jemand zum Beispiel das hier mit OKI gemacht hat:

```
User: wie gehts dir oki?
OKI: wie gehts dir?
User: Was geht dich das an, du Penner?
```

Wird das so in die Brain-Datei gespeichert:

```
Q:wie gehts dir?
A:Was geht dich das an, du Penner?
```

Einfach die entschprechenden Zeilen löschen. Sind es jedoch mehrere Antworten, wie zum Beispiel hier:

```
User: Oki würfeln
OKI: würfeln
User: Eins~Zwei~Drei~Vier~Fünf~Sechs
```

wird das so abgespeichert:

```
Q:würfeln
A:Eins
A:Zwei
A:Drei
A:Vier
A:Fünf
A:Sechs
```

Die entsprechende Zeile kann dann einfach abgeändert oder gelöscht werden.
#### WICHTIG: Wichtig ist, dass die letzte Zeile der Datei, eine leere Zeile ist!

### Die BlackList Datei bearbeiten

Um bestimmte Schimpfwörter zu verhindern, gibt es die Blacklist. Sie reiht einfach nur die Wörter auf.
Einfach in eine neue Zeile ein neues Wort hinzufügen, klein geschrieben und ohne Sonderzeichen (ä, ö, ü, etc. sind jedoch erlaubt).
Ein Beispiel für die Datei:

```
penner
scheiße
...
```

(Nun gut, das `...` sollte darstellen, dass die Datei weitergeht. Sowas sollte natürlich nicht in der Datei stehen.)
Enthält eine Nachricht/Antwort eines der angeführten Wörter, ignoriert OKI die Nachricht/Antwort.

#### AUCH HIER: Wichtig ist, dass die letzte Zeile der Datei, eine leere Zeile ist!
