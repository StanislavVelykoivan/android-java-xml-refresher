"""Local teaching API. Python 3, no dependencies. Data is illustrative."""
import argparse
import hashlib
import json
import struct
import time
import zlib
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlsplit, parse_qs, urlencode, quote

KEY = "animals-local-key"
PHOTO_FILES = ["Cat03.jpg", "YellowLabradorLooking_new.jpg", "African_Bush_Elephant.jpg",
               "Giraffe_in_profile.jpg", "Lion_waiting_in_Namibia.jpg", "European-rabbits-1006621.jpg"]

def photo_url(index):
    filename = PHOTO_FILES[index]
    digest = hashlib.md5(filename.encode()).hexdigest()
    return f"https://upload.wikimedia.org/wikipedia/commons/{digest[0]}/{digest[:2]}/{quote(filename)}"
DATA = [
    ("Cat", "Felidae", "Carnivora", "Worldwide", 48, 15, "Carnivore"),
    ("Dog", "Canidae", "Carnivora", "Worldwide", 45, 12, "Omnivore"),
    ("Elephant", "Elephantidae", "Proboscidea", "Africa and Asia", 25, 60, "Herbivore"),
    ("Giraffe", "Giraffidae", "Artiodactyla", "Africa", 50, 25, "Herbivore"),
    ("Lion", "Felidae", "Carnivora", "Africa", 80, 15, "Carnivore"),
    ("Rabbit", "Leporidae", "Lagomorpha", "Worldwide", 40, 9, "Herbivore"),
]

def animals(base):
    return [dict(name=n, taxonomy=dict(kingdom="Animalia", order=o, family=f),
                 location=l, speed=dict(metric=f"{s} km/h", imperial=f"{s / 1.609344:.1f} mph"),
                 diet=d, lifespan=f"{age} years", image=photo_url(i),
                 imageSource="https://commons.wikimedia.org/wiki/File:" + quote(PHOTO_FILES[i]))
            for i, (n, f, o, l, s, age, d) in enumerate(DATA)]

def placeholder(index):
    # Solid-colour PNG fixtures, intentionally not animal photographs.
    colours = [(220, 145, 90), (100, 150, 210), (140, 150, 160),
               (240, 190, 70), (190, 120, 60), (170, 130, 190)]
    rgb = bytes(colours[index])
    def chunk(kind, data):
        return struct.pack('!I', len(data)) + kind + data + struct.pack('!I', zlib.crc32(kind + data))
    pixels = (b'\x00' + rgb * 128) * 128
    return (b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('!2I5B', 128, 128, 8, 2, 0, 0, 0))
            + chunk(b'IDAT', zlib.compress(pixels)) + chunk(b'IEND', b''))

class Handler(BaseHTTPRequestHandler):
    def do_POST(self):
        if urlsplit(self.path).path.rstrip('/') != '/getAnimals':
            return self.reply(404, {"message": "Not found"})
        try:
            size = int(self.headers.get('Content-Length', '0'))
            if not 0 <= size <= 8192:
                return self.reply(413, {"message": "Request too large"})
            body = self.rfile.read(size).decode('utf-8')
            fields = parse_qs(body)
        except (ValueError, UnicodeDecodeError):
            return self.reply(400, {"message": "Invalid form body"})
        parsed = urlsplit(self.path)
        params = parse_qs(parsed.query)
        params.update(fields)
        self.path = parsed.path + '?' + urlencode(params, doseq=True)
        self.do_GET()

    def reply(self, status, payload, mime="application/json; charset=utf-8"):
        body = payload if isinstance(payload, bytes) else json.dumps(payload).encode()
        self.send_response(status)
        self.send_header("Content-Type", mime)
        self.send_header("Content-Length", str(len(body)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(body)

    def do_GET(self):
        parsed = urlsplit(self.path)
        params = parse_qs(parsed.query)
        try:
            delay = min(10, max(0, float(params.get("delay", ["0"])[0])))
            status = int(params.get("status", ["200"])[0])
            if status not in (200, 400, 401, 403, 404, 429, 500, 503):
                raise ValueError()
        except ValueError:
            return self.reply(400, {"message": "Invalid delay or status"})
        time.sleep(delay)
        if status != 200:
            return self.reply(status, {"message": "Simulated API error"})
        path = parsed.path.rstrip("/") or "/"
        base = self.server.public_url or "http://" + self.headers.get("Host", "localhost:8080")
        if path in ("/", "/health"):
            return self.reply(200, {"message": "Animals API running"})
        if path == "/getKey":
            return self.reply(200, {"message": "Success", "key": KEY})
        if path == "/getAnimals":
            if params.get("key", [None])[0] != KEY:
                return self.reply(401, {"message": "Invalid API key"})
            return self.reply(200, [] if params.get("empty") == ["true"] else animals(base))
        if path == "/api/animals.json":
            rows = [dict(name=n, taxonomy=dict(kingdom="Animalia", order=o, family=f),
                         range={"nativeRange": l}, stats={"topSpeedKmh": s, "lifespanYears": age},
                         facts=[{"label": "Diet", "value": d}]) for n, f, o, l, s, age, d in DATA]
            return self.reply(200, {"animals": [] if params.get("empty") == ["true"] else rows})
        if path.startswith("/images/"):
            try:
                index = int(path.rsplit("/", 1)[-1].removesuffix(".png"))
                if not 0 <= index < len(DATA):
                    raise ValueError()
                return self.reply(200, placeholder(index), "image/png")
            except ValueError:
                pass
        self.reply(404, {"message": "Not found"})

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--host", default="0.0.0.0")
    parser.add_argument("--port", type=int, default=8080)
    parser.add_argument("--public-url", default="")
    args = parser.parse_args()
    server = ThreadingHTTPServer((args.host, args.port), Handler)
    server.public_url = args.public_url.rstrip("/")
    print(f"Animals API: http://{args.host}:{args.port} (Ctrl+C to stop)", flush=True)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()

if __name__ == "__main__":
    main()
