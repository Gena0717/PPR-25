function createRadarChart(sentiments, width, height) {
    var margin = 30;

    const svg = d3.create("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("viewBox", [0, 0, width + margin, height + margin])
        .attr("style", "max-width: 100%; height: auto; font: 10px sans-serif; background-color:var(--background-color-light);")
        .attr("text-anchor", "middle");

    let posTemp = 0;
    let negTemp = 0;
    let neuTemp = 0;
    sentiments.map(d => {
        posTemp += d.pos === null ? 0 : Number(d.pos) || 0;
        negTemp += d.neg === null ? 0 : Number(d.neg) || 0;
        neuTemp += d.neu === null ? 0 : Number(d.neu) || 0;
    });
    const data = [posTemp / sentiments.length, negTemp / sentiments.length, neuTemp / sentiments.length];

    const numAxes = 3;
    const angleRad = Math.PI * 2 / numAxes;
    const radius = Math.min(width, height) / 2 - margin;
    const axisLabels = ["POS", "NEG", "NEU"];

    const grid = svg.append("g")
        .attr("transform", `translate(${width / 2 + margin}, ${height / 2 + margin})`);

    const radiusScale = d3.scaleLinear()
        .domain([0, 1])
        .range([0, radius]);

    grid.selectAll(".gridCircle")
        .data(d3.range(1, 4))
        .enter().append("circle")
        .attr("class", "gridCircle")
        .attr("r", d => radiusScale(d * 1 / 3))
        .style("fill", "#313843")
        .style("stroke", "#76808c")
        .style("stroke-width", "0.5px")
        .style("fill-opacity", 0.1);

    grid.selectAll(".axisLine")
        .data(sentiments)
        .enter().append("line")
        .attr("class", "axisLine")
        .attr("x1", 0)
        .attr("y1", 0)
        .attr("x2", (d, i) => radius * Math.cos(i * angleRad - Math.PI / 2))
        .attr("y2", (d, i) => radius * Math.sin(i * angleRad - Math.PI / 2))
        .style("stroke", "#76808c")
        .style("stroke-width", "1px");

    grid.selectAll(".axisLabel")
        .data(axisLabels)
        .enter().append("text")
        .attr("class", "axisLabel")
        .attr("x", (d, i) => (radius + 10) * Math.cos(i * angleRad - Math.PI / 2))
        .attr("y", (d, i) => (radius + 10) * Math.sin(i * angleRad - Math.PI / 2))
        .attr("dy", "0.35em")
        .style("text-anchor", "middle")
        .text(d => d)
        .style("font-size", "12px")
        .style("fill", "white");

    const radarLine = d3.lineRadial()
        .radius(d => radiusScale(d))
        .angle((d, i) => i * angleRad);

    grid.append("path")
        .datum(data)
        .attr("class", "radarArea")
        .attr("d", radarLine)
        .style("fill", "#66c0f4")
        .style("fill-opacity", 0.9)
        .style("stroke", "white")
        .style("stroke-width", "1px");

    grid.selectAll(".dataPoint")
        .data(data)
        .enter().append("circle")
        .attr("class", "dataPoint")
        .attr("cx", (d, i) => radiusScale(d) * Math.cos(i * angleRad - Math.PI / 2))
        .attr("cy", (d, i) => radiusScale(d) * Math.sin(i * angleRad - Math.PI / 2))
        .attr("r", 2)
        .style("fill", "white");

    return svg.node();
}