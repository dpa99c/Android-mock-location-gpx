# Android mock location gpx
An application for easy mocking of locations via a GPX file.

## Getting Started
After installation set this app in device developer options to be the mock location app.

The app mocks locations via a track file in [GPX format](https://wiki.openstreetmap.org/wiki/GPX).

Each `<trkpt>` must have:
- `lat` and `lon` attributes
- a `<time>` element with the value in format `yyyy-MM-ddTHH:mm:ssZ` or `yyyy-MM-ddTHH:mm:ss.SSSZ`,

and optionally:
- `<ele>` elements for elevation. e.g. `<ele>105.32</ele>`
- `<speed>` elements for speed in m/s. e.g. `<speed>2.5</speed>`
- `<accuracy>` elements for accuracy in meters. e.g. `<accuracy>7.41</accuracy>`

An example GPX track file can be found here: [`mock_track.gpx`](/mock_track.gpx)

GPX track files must be put on the device here:
```
/sdcard/Mocks/
```

The app should be granted read external storage and location permissions.
Mock route can be started from the app UI by tapping on "RUN MOCK ROUTE" and chosing the GPX file via the File Picker UI.
Location mocking can be stopped by tapping "CLEAR MOCK ROUTE".