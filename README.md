# jersey-hateos-example
Jersey 2 project which show an example of hateos hyperlinking


# example

````
mvn clean package exec:java
curl http://localhost:8080/myapp/boxes
````

response

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<boxes>
  <box>
    <id>637afdfd-a1e8-40ca-a0a4-de34dd145b3a</id>
    <ip>226.214.176.14</ip>
    <name>pbbrUKA</name>
    <uri>/myapp/boxes/637afdfd-a1e8-40ca-a0a4-de34dd145b3a</uri>
    <users>
      <user>
        <uri>http://localhost:8080/myapp/users/f3785e86-7229-454a-a0de-0ad1497b609f</uri>
        <firstName>g0Xki-Q</firstName>
        <lastName>qZbvwJ4</lastName>
      </user>
      <user>
        <uri>http://localhost:8080/myapp/users/e08f7d14-ef71-423c-ac2e-b8af7ddb2898</uri>
        <firstName>Ml67gbw</firstName>
        <lastName>JjWUp-M</lastName>
      </user>
    </users>
  </box>
  <box>
    <id>d4386fd0-5b65-4c65-a0a2-f6f32e6cd6eb</id>
    <ip>208.54.148.101</ip>
    <name>Qheyz6s</name>
    <uri>/myapp/boxes/d4386fd0-5b65-4c65-a0a2-f6f32e6cd6eb</uri>
    <users>
      <user>
        <uri>http://localhost:8080/myapp/users/efe0cced-55d7-4636-80b7-3f14706c885c</uri>
        <firstName>XVerUUk</firstName>
        <lastName>UG8ohYQ</lastName>
      </user>
      <user>
        <uri>http://localhost:8080/myapp/users/a398d27e-0248-4553-854c-652633004faa</uri>
        <firstName>IVpY_-c</firstName>
        <lastName>XMtPD1I</lastName>
      </user>
    </users>
  </box>
</boxes>
```



