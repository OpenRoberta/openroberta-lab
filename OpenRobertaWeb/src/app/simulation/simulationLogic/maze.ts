export function maze(x, y) {
    var n = x * y - 1;
    if (n < 0) { alert("illegal maze dimensions"); return; }
    var horiz = []; for (var j = 0; j < x + 1; j++) horiz[j] = [];
    var verti = []; for (var j = 0; j < y + 1; j++) verti[j] = [];
    // console.log(display({ x: x, y: y, horiz: horiz, verti: verti }));
    var here = [Math.floor(Math.random() * x), Math.floor(Math.random() * y)];
    var path = [here];
    var unvisited = [];
    for (var j = 0; j < x + 2; j++) {
        unvisited[j] = [];
        for (var k = 0; k < y + 1; k++)
            unvisited[j].push(j > 0 && j < x + 1 && k > 0 && (j != here[0] + 1 || k != here[1] + 1));
    }
    while (0 < n) {
        var potential = [[here[0] + 1, here[1]], [here[0], here[1] + 1],
        [here[0] - 1, here[1]], [here[0], here[1] - 1]];
        var neighbors = [];
        for (var j = 0; j < 4; j++)
            if (unvisited[potential[j][0] + 1][potential[j][1] + 1])
                neighbors.push(potential[j]);
        if (neighbors.length) {
            n = n - 1;
            let next = neighbors[Math.floor(Math.random() * neighbors.length)];
            unvisited[next[0] + 1][next[1] + 1] = false;
            if (next[0] == here[0])
                horiz[next[0]][(next[1] + here[1] - 1) / 2] = true;
            else
                verti[(next[0] + here[0] - 1) / 2][next[1]] = true;
            path.push(here = next);
        } else
            here = path.pop();
    }
    return ({ x: x, y: y, horiz: horiz, verti: verti });
}

export function display(m) {
    var text = [];
    for (var j = 0; j < m.x * 2 + 1; j++) {
        var line = [];
        if (0 == j % 2)
            for (var k = 0; k < m.y * 4 + 1; k++)
                if (0 == k % 4)
                    line[k] = '+';
                else
                    if (j > 0 && m.verti[j / 2 - 1][Math.floor(k / 4)])
                        line[k] = ' ';
                    else
                        line[k] = '-';
        else
            for (var k = 0; k < m.y * 4 + 1; k++)
                if (0 == k % 4)
                    if (k > 0 && m.horiz[(j - 1) / 2][k / 4 - 1])
                        line[k] = ' ';
                    else
                        line[k] = '|';
                else
                    line[k] = ' ';
        if (0 == j) line[1] = line[2] = line[3] = ' ';
        if (m.x * 2 - 1 == j) line[4 * m.y] = ' ';
        text.push(line.join('') + '\r\n');
    }
    let maze = text.join('');
    console.log(m);

    console.log(maze);
    download(m);
    return maze;
}

function download(m) {
    let l = 1 / (m.x + 0.2);
    let b = 1 / (m.y + 0.2);
    let w = 0.1 * b;
    var result = { "robotPoses": [], "obstacles": [], "colorAreas": [], "ruler": {} };
    result.robotPoses.push({ "x": b / 2 + w, "y": w, "theta": Math.PI / 2, "xOld": 0, "yOld": 0, "transX": 0, "transY": 0, "thetaOld": 0, "thetaDiff": 0 });
    result.colorAreas.push({ "x": 0 + w, "y": 0, "h": l / 2 + w, "w": b, "form": "rectangle", "type": "colorArea", "theta": 0, "color": "green" });
    result.colorAreas.push({ "x": (m.y - 1) * b + b / 2 + w, "y": (m.x - 1) * l + w, "h": l, "w": b / 2 + w, "form": "rectangle", "type": "colorArea", "theta": 0, "color": "green" });
    result.ruler = { "x": 0, "y": 0, "xOld": 0, "yOld": 0, "w": 0, "h": 0, "wOld": 0, "hOld": 0, "type": "ruler", "img": null, "color": null };
    for (var i = 0; i < m.x; i++) {
        for (var j = 0; j < m.y - 1; j++) {
            if (!m.horiz[i][j]) {
                let x = (j + 1) * b + w / 2;
                let y = (i) * l + w;
                let h = l;
                for (var k = i + 1; k < m.x; k++) {
                    if (!m.horiz[k][j]) {
                        h += l;
                        m.horiz[k][j] = true;
                    } else {
                        break;
                    }
                }
                result.obstacles.push({ "x": x, "y": y, "h": h, "w": w, "form": "rectangle", "type": "obstacle", "theta": 0, "color": "#33B8CA" });
            }
        }
    }
    for (var i = 0; i < m.x - 1; i++) {
        for (var j = 0; j < m.y; j++) {
            if (!m.verti[i][j]) {
                let x = (j) * b + w;
                let y = (i + 1) * l + w / 2;
                let h = 0.1 * l;
                let wx = b;
                for (var k = j + 1; k < m.y; k++) {
                    if (!m.verti[i][k]) {
                        wx += b;
                        m.verti[i][k] = true;
                    } else {
                        break;
                    }
                }
                result.obstacles.push({ "x": x, "y": y, "h": h, "w": wx, "form": "rectangle", "type": "obstacle", "theta": 0, "color": "#33B8CA" });
            }
        }
    }
    console.log(JSON.stringify(result));
}
