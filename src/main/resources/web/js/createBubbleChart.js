function createBubbleChart(topics, width, height) {
    var margin = 1;

    const svg = d3.create("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("viewBox", [-margin, -margin, width, height])
        .attr("style", "max-width: 100%; height: auto; font: 10px sans-serif; background-color:var(--background-color-light);")
        .attr("text-anchor", "middle");

    const data = getHighestScores(topics);

    const format = d3.format(".2f");
    const color = d3.scaleOrdinal(d3.schemeTableau10);
    const pack = d3.pack()
        .size([width - margin * 2, height - margin * 2])
        .padding(3);

    const root = pack(d3.hierarchy({children: data})
        .sum(d => d.score));

    const node = svg.append('g')
        .selectAll()
        .data(root.leaves())
        .enter()
        .append('g')
        .attr("transform", d => `translate(${d.x}, ${d.y})`);

    node.append("title")
        .text(d => `${d.data.value}\n${format(d.value)}`);

    node.append("circle")
        .attr("fill-opacity", 0.6)
        .attr("fill", d => color(d.r))
        .attr("r", d => d.r);

    const text = node.append("text")
        .attr("clip-path", d => `circle(${d.r})`)
        .style("fill", "white");

    text.selectAll()
        .data(d => d.data.value.split(/\s+/))
        .enter()
        .append("tspan")
        .attr("x", 0)
        .attr("y", (d, i, nodes) => `${i - nodes.length / 2 + 0.35}em`)
        .text(d => d);

    text.append("tspan")
        .attr("x", 0)
        .attr("y", `-1em`)
        .attr("fill-opacity", 0.6)
        .text(d => format(d.data.score));

    return svg.node();
}

function getHighestScores(topics) {
    const topicMap = new Map();

    topics.forEach(({value, score}) => {
        const numScore = Number(score);
        if (!topicMap.has(value) || numScore > topicMap.get(value).score) {
            topicMap.set(value, {value, score: numScore});
        }
    });

    return Array.from(topicMap.values());
}