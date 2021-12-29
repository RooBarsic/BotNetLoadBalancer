var Paint = {
    pixel: { size: 4 },
    grid: { numRows: 100, numCols: 100 }
};

window.onload = function () {
    console.log("Helloiiii");

    console.log("hella");
    draggable_1 = new PlainDraggable(document.getElementById("draggable_1"));
    draggable_2 = new PlainDraggable(document.getElementById("draggable_2"));
    draggable_3 = new PlainDraggable(document.getElementById("draggable_3"));
    draggable_4 = new PlainDraggable(document.getElementById("draggable_4"));
    // draggable.snap = {step: 30};


    var canvas = document.getElementById('paintCanvas'),
        context = canvas.getContext('2d'),
        offset = getOffset(canvas, document.body),
        pixelSize = Paint.pixel.size,
        numRows = Paint.grid.numRows,
        numCols = Paint.grid.numCols,
        painting = false;

    canvas.width = numCols * pixelSize;
    canvas.height = numRows * pixelSize;

    window.onkeydown = function (event) {

        var code = event.which;
        if (code == 17 || code == 16) {
            painting = true;
            context.fillStyle = (code == 17 ? '#1b6bb5' : '#b53a31');
        }
        if (code == 17) {
            line = new LeaderLine(
                document.getElementById('draggable_1'),
                document.getElementById('draggable_2'),
                {dash: {animation: true}}
            );

        }
        if (code == 16) {
            line = new LeaderLine(
                document.getElementById('draggable_2'),
                document.getElementById('draggable_1'),
                {dash: {animation: true}}
            );

        }
    };

    window.onkeyup = function (event) {
        var code = event.which;
        if (code == 17 || code == 16) {
            painting = false;
        }
    };

    canvas.onmousemove = function (event) {
        console.log("mouse");
        if (!painting) {
            return;
        }
        event = event || window.event;
        var mouse = getMousePosition(event),
            x = mouse.x - offset.left,
            y = mouse.y - offset.top;
        x -= x % pixelSize;
        y -= y % pixelSize;
        context.fillRect(x, y, pixelSize, pixelSize);
    };
};

function getOffset(element, ancestor) {
    var left = 0,
        top = 0;
    while (element != ancestor) {
        left += element.offsetLeft;
        top += element.offsetTop;
        element = element.parentNode;
    }
    return { left: left, top: top };
}

function getMousePosition(event) {
    if (event.pageX !== undefined) {
        return { x: event.pageX, y: event.pageY };
    }
    return {
        x: event.clientX + document.body.scrollLeft +
            document.documentElement.scrollLeft,
        y: event.clientY + document.body.scrollTop +
            document.documentElement.scrollTop
    };
}
