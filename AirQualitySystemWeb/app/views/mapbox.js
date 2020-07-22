mapboxgl.accessToken = 'pk.eyJ1IjoidGFpc2lpYWlud29uZGVybGFuZCIsImEiOiJja2F0djU0MDIweDh3MnhvYzdudjl3b3plIn0.pzSVZndRw9SOzDdkkazTHQ';

let map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v9',
    zoom: 11,
    pitchWithRotate: false, //"drag to rotate" interaction is disabled
    clickTolerance: 4, //Максимальное количество пикселей, которое пользователь может сместить указатель мыши во время клика, чтобы он считался действительным кликом
    logoPosition: 'bottom-right',
    fadeDuration: 500, //плавность появления
    
    //preserveDrawingBuffer: true, //the map's canvas can be exported to a PNG using map.getCanvas().toDataURL()
    center: [30.524808, 50.448624]
});

let nav = new mapboxgl.NavigationControl({
    showZoom: true
});
map.addControl(nav, 'bottom-right');

map.on('load', function() {
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
            //  "text-offset": [0, 0.6],
            //  "text-anchor": "top"
        }
    });
})


