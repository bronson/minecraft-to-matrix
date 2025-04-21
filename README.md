# Minecraft To Matrix

A simple Minecraft plugin to publish player events to a Matrix room.

## Install

Copy the plugin's jar to your server's `plugins` directory.

Restart the server.

Now you'll see a plugins/MinecraftToMatrix/config.yml file.

Fill in the file with your server name, room ID, username, and password.

In Element, you find the room ID in the room's Settings -> Advanced ->
Internal Room ID.

```yml
matrix:
  server: https://matrix.org
  room: '!ROOM-ID:matrix.org'
  username: jdoe
  password: 'onoes!'
```

NOTE: right now the server URL only specifies the hostname. The plugin
always uses the default port and protocol.
Follow [the upstream issue](https://github.com/Cosium/matrix-communication-client/issues/92) for updates.

## Usage

Now restart your server. Hope it worked!

Send me patches.

## License

My code is MIT licensed, use it however you want. The rest of the code
in this project retains its original license.
