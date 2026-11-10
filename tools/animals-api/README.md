# Local Animals API

A small Python 3 API for running the Android demo locally. It uses only the Python standard library and returns illustrative animal data. No API key or external service account is required.

Run it from the repository root:

```powershell
python tools/animals-api/server.py
```

The server listens on port `8080`. For Android Emulator, the app connects to it at `http://10.0.2.2:8080/`. For a USB-connected device, use `adb reverse tcp:8080 tcp:8080` and configure the app to use `http://127.0.0.1:8080/` in `local.properties`.

The API supplies animal photo URLs from Wikimedia Commons and includes the corresponding Commons file-page URL in each animal's `imageSource` field. Check the individual file page for the current author and license terms before reusing an image outside this demo.

This server is for local development only. Its fixed demo key does not provide security, and the HTTP endpoint is not intended for production use.
