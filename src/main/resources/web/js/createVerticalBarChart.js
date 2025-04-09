function createVerticalBarChart(pos, width, height){
    var margin = 30;

    const svg = d3.create("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("viewBox", [-margin / 2, -1.5 *margin, width + margin, height + margin])
        .attr("style", "max-width: 100%; height: auto; font: 10px sans-serif; background-color:var(--background-color-light);")
        .attr("text-anchor", "middle");

    const counts = d3.rollup(pos, v => v.length, d => d.coarseValue);
    const data = Array.from(counts, ([coarseValue, count]) => ({coarseValue, count}))
        .sort((a, b) => d3.descending(a.count, b.count));

    const x = d3.scaleBand()
        .domain(data.map(d => d.coarseValue))
        .range([0, width - margin])
        .padding(0.2);

    const y = d3.scaleLinear()
        .domain([0, d3.max(data, d => d.count)])
        .nice()
        .range([height - margin, 0]);

    const color = d3.scaleOrdinal(d3.schemeTableau10);

    const g = svg.append("g")
        .attr("transform", `translate(${margin},${-margin})`);

    g.selectAll("rect")
        .data(data)
        .enter()
        .append("rect")
        .attr("x", d => x(d.coarseValue))
        .attr("y", d => y(d.count))
        .attr("height", d => (height - margin) - y(d.count))
        .attr("width", x.bandwidth())
        .attr("fill", d => color(d.coarseValue));

    g.append("g")
        .attr("transform", `translate(0,${(height - margin)})`)
        .call(d3.axisBottom(x))
        .selectAll("text")
        .attr("transform", "rotate(-45)")
        .attr("text-anchor", "end");

    g.append("g").call(d3.axisLeft(y));

    return svg.node();
}