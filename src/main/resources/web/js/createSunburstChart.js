function createSunburstChart(namedEntities, width, height) {
    var margin = 30;

    const svg = d3.create("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("viewBox", [-width /2, -height/2, width, height])
        .attr("style", "max-width: 100%; height: auto; font: 10px sans-serif; background-color:var(--background-color-light);")
        .attr("text-anchor", "middle");

    const counts = d3.rollup(namedEntities, v => v.length, d => d.value);
    const data = Array.from(counts, ([value, count]) => ({value, count}))
        .sort((a, b) => d3.descending(a.count, b.count));

    const radius = Math.min(width, height) / 2 - margin;

    const root = d3.hierarchy({ name: "root", children: data })
        .sum(d => d.count)
        .sort((a, b) => b.value - a.value);

    const partition = d3.partition()
        .size([2 * Math.PI, radius]);

    partition(root);

    const arc = d3.arc()
        .startAngle(d => d.x0)
        .endAngle(d => d.x1)
        .innerRadius(d => d.y0)
        .outerRadius(d => d.y1);

    const color = d3.scaleOrdinal(d3.schemeCategory10);

    const node = svg.append("g")
        .selectAll("path")
        .data(root.descendants().slice(1))
        .enter().append("path")
        .attr("d", arc)
        .attr("fill", d => color(d.data.value))
        .style("stroke", "#fff");

    node.selectAll("text")
        .data(root.descendants().slice(1))
        .enter().append("text")
        .attr("transform", d => {
            const x = (d.x0 + d.x1) / 2 * 180 / Math.PI;
            const y = (d.y0 + d.y1) / 2;
            return `rotate(${x - 90}) translate(${y},0) rotate(${x < 180 ? 0 : 180})`;
        })
        .attr("dy", "0.35em")
        .text(d => d.data.value)
        .style("font-size", "10px")
        .style("fill", "white");

    svg.append("g")
        .selectAll("text")
        .data(root.descendants().slice(1))
        .enter().append("text")
        .attr("transform", d => {
            const angle = ((d.x0 + d.x1) / 2) * 180 / Math.PI;
            const r = (d.y0 + d.y1) / 2- 40;
            const x = r * Math.cos((d.x0 + d.x1) / 2 - Math.PI / 2);
            const y = r * Math.sin((d.x0 + d.x1) / 2 - Math.PI / 2);
            const rotation = angle < 180 ? angle - 90 : angle + 90;

            return `translate(${x},${y}) rotate(${rotation})`;
        })
        .attr("dy", "0.35em")
        .attr("dx", d => ((d.x0 + d.x1) / 2 > Math.PI ? "-0.35em" : "0.35em"))
        .text(d => d.data.value)
        .style("font-size", d => Math.max(10, (d.y1 - d.y0) / 5) + "px")
        .style("fill", "white")
        .style("text-anchor", d => ((d.x0 + d.x1) / 2 > Math.PI ? "end" : "start"));

    return svg.node();
}