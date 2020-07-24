//mapboxgl.accessToken = 'pk.eyJ1IjoidGFpc2lpYWlud29uZGVybGFuZCIsImEiOiJja2F0djU0MDIweDh3MnhvYzdudjl3b3plIn0.pzSVZndRw9SOzDdkkazTHQ';
mapboxgl.accessToken = 'pk.eyJ1IjoidGFpc2lpYWlud29uZGVybGFuZCIsImEiOiJja2F0djU0MDIweDh3MnhvYzdudjl3b3plIn0.pzSVZndRw9SOzDdkkazTHQ';

let map = new mapboxgl.Map({
    container: 'map',
    //zoom:9,
    //center:[30.524808, 50.448624],
    style: 'mapbox://styles/mapbox/streets-v9',
    zoom: 11,
    pitchWithRotate: false, //"drag to rotate" interaction is disabled
    clickTolerance: 4, //Максимальное количество пикселей, которое пользователь может сместить указатель мыши во время клика, чтобы он считался действительным кликом
    logoPosition: 'bottom-right',
    fadeDuration: 500, //плавность появления

    //preserveDrawingBuffer: true, //the map's canvas can be exported to a PNG using map.getCanvas().toDataURL()
    center: [30.524808, 50.448624]
});

let monument = [30.471391, 50.383328];

let nav = new mapboxgl.NavigationControl({
    showZoom: true
});
map.addControl(nav, 'bottom-right');

map.on('load', function() {
    map.addLayer({
        'id': 'back',
        'type': 'background',
        'source': {
            'type': 'geojson',
            'data': 'point'
        },
        'paint': {
            'background-color': 'rgba(0,128,128,0.1)'
        }
    });
    // map.loadImage(
    //     'https://upload.wikimedia.org/wikipedia/commons/7/7c/201408_cat.png',
    //     function(error, image) {
    //         if (error) throw error;
    //         map.addImage('cat', image);
    //         map.addSource('point', {
    //             'type': 'geojson',
    //             'data': {
    //                 'type': 'FeatureCollection',
    //                 'features': [
    //                     {
    //                         'type': 'Feature',
    //                         'geometry': {
    //                             'type': 'Point',
    //                             'coordinates': [30.524808, 50.448624]
    //                         }
    //                     }
    //                 ]
    //             }
    //         });
    //         map.addLayer({
    //             'id': 'cat',
    //             'type': 'symbol',
    //             'source': 'point',
    //             'layout': {
    //                 'icon-image': 'cat',
    //                 'icon-size': 0.4,
    //                 'icon-rotate': 10
    //             },
    //             'paint': {
    //                 'icon-opacity': 0.5
    //             }
    //         });
    //     }
    // );
    map.addLayer({
        "id": "points",
        "type": "symbol",
        "source": {
            "type": "geojson",
            "data": points
        },
        "layout": {
            "icon-image": "{icon}-15",
            "text-field": "{title}",
            "text-font": ["Open Sans Semibold",
                "Arial Unicode MS Bold"],
            "icon-size": 1.5
            //  "text-offset": [0, 0.6],
            //  "text-anchor": "top"
        },
        "paint": {
            "icon-opacity": 0.9,
            "icon-color": "green",
            "text-color": "white"
        }
    });
});

let popup = new mapboxgl.Popup({ offset: 25 }).setText(
    'Construction on the Washington Monument began in 1848.'
);

// create DOM element for the marker
let el = document.createElement('div');
el.id = 'marker';

// create the marker
let marker = new mapboxgl.Marker({
    element: el,
    draggable: true
})
    .setLngLat(monument)
    .setPopup(popup) // sets a popup on this marker
    .addTo(map);

function onDragEnd() {
    var lngLat = marker.getLngLat();
    popup.setHTML('Longitude: ' + lngLat.lng + '<br />Latitude: ' + lngLat.lat);
}

marker.on('dragend', onDragEnd);
