# Minecraft To Matrix

A simple Minecraft plugin to publish player events and chat to a Matrix room.

## Install


1. Copy the plugin's jar to your server's `plugins` directory.

1. Restart the server.

Now you'll see a `plugins/MinecraftToMatrix/config.yml` file.

Fill in the file with your info. 
In Element, you can find the room ID in the room's Settings -> Advanced ->
Internal Room ID.

```yml
matrix:
  server: https://matrix.org
  room: '!ROOM-ID:matrix.org'
  username: jdoe
  password: 'onoes!'
```

## Usage

Now that the configuration is set, restart your server again.

Enjoy the messages!

Send me patches.

### Compatibility

I'm using this plugin on a Paper 1.21.4 server.

## License

The code written by me is MIT licensed. Use it however you want.

Any other code in this project retains its original license.
