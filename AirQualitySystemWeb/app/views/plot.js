function getData() {
    return Math.random();
}

let trace = {
    x: [getData(), getData(), getData()],
    y: [getData(), getData(), getData()],
    mode: 'lines',
    name: 'Red',
    line: {
        color: 'rgb(55, 128, 191)',
        width: 3
    }
};
let data = [trace];

var layout = {
    showlegend: false
};

let text = {
    annotations: [{
        xref: 'paper',
        yref: 'paper',
        x: 0,
        xanchor: 'right',
        y: 1,
        yanchor: 'bottom',
        text: 'X axis label',
        showarrow: false
    }, {
        xref: 'paper',
        yref: 'paper',
        x: 1,
        xanchor: 'left',
        y: 0,
        yanchor: 'top',
        text: 'Y axis label',
        showarrow: false
    }]
}

Plotly.newPlot('chart', data, text, layout, {scrollZoom: true});


