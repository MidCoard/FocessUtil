# FocessUtil — DEPRECATED

> ⚠ **This library is deprecated and no longer maintained.**
>
> The final release is `1.1.25`, published only to mark all APIs as `@Deprecated(forRemoval = true)`.
> The source behavior in `1.1.25` is identical to `1.1.24`; the only changes are deprecation
> annotations and this notice. There will be no further releases and no security fixes.

## Why

FocessUtil was a grab-bag utility library wrapping Guava, Jackson, SnakeYAML, XStream, OkHttp,
and a hand-rolled RSA helper. Maintaining it is not justified:

- The `top.focess.util.RSA` class uses **1024-bit RSA keys** and **`MD5withRSA`** signatures.
  Both are considered insecure by modern standards and have been for years. **Do not use it
  in any version.**
- Most other classes duplicate functionality already provided by widely-used libraries
  (Commons-Lang `Pair`, Commons-Codec `Base64`, Jackson/SnakeYAML directly, etc.).

## Migration suggestions

| FocessUtil class | Recommended replacement |
| --- | --- |
| `top.focess.util.Pair` | `org.apache.commons.lang3.tuple.Pair` |
| `top.focess.util.Base64` | `java.util.Base64` (JDK ≥ 8) |
| `top.focess.util.RSA` / `RSAKeypair` | BouncyCastle, or `javax.crypto` directly with `RSA/ECB/OAEPWithSHA-256AndMGF1Padding` and ≥ 2048-bit keys; **never `MD5withRSA`** |
| `top.focess.util.json.*` | Jackson (`com.fasterxml.jackson.*`) directly |
| `top.focess.util.yaml.*` | SnakeYAML directly |
| `top.focess.util.serialize.*` | Jackson, kryo, or `java.io.Serializable` as appropriate |
| `top.focess.util.network.*` | OkHttp directly, or `java.net.http.HttpClient` (JDK ≥ 11) |
| `top.focess.util.option.*` | picocli, JCommander, or Apache Commons CLI |
| `top.focess.util.version.*` | `org.apache.maven.artifact.versioning.ComparableVersion`, or `com.vdurmont:semver4j` |
| `top.focess.util.SectionMap` | SnakeYAML `Map<String, Object>` directly, or Typesafe Config |

## Last release

```xml
<dependency>
    <groupId>top.focess</groupId>
    <artifactId>focess-util</artifactId>
    <version>1.1.25</version>
</dependency>
```

The repository will be archived on GitHub after this release.
