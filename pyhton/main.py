from nbiot import nbiot
import asyncio
import time
import codecs


class Receiver:

    def __init__(self):
        self.received = []
        self.payload = []
        self.got_msg = False
        self.last_save_time = time.perf_counter()
        self.time_between_saves = 60 * 15  # seconds
        self.files_saved = 0

    async def wait_on_msg(self):
        client = nbiot.Client()
        stream = await client.collection_output_stream('XXXXXXXXX')
        while True:
            try:
                msg = await stream.recv()
            except nbiot.OutputStreamClosed:
                break
            # msg.received += timedelta(hours=1)
            self.received.append(msg.received.strftime("%m/%d/%Y, %H:%M:%S"))
            self.payload.append(msg.payload)
            print(msg.received)
            print(msg.payload)
            print()
            self.got_msg = True

    async def save_msg(self):
        while True:
            if self.got_msg and time.perf_counter() - self.last_save_time > self.time_between_saves:
                self.files_saved += 1
                f = codecs.open(f"data{self.files_saved:d}.txt", "w", "utf-8")
                for i in range(len(self.received)):
                    f.write(self.received[i] + '\n')
                    f.write(self.payload[i].decode("utf-8") + '\n\n')
                f.close()
                self.received = []
                self.payload = []
                self.last_save_time = time.perf_counter()
                self.got_msg = False
            else:
                await asyncio.sleep(5)

    async def main(self):
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)
        await asyncio.gather(self.wait_on_msg(), self.save_msg())


receiver = Receiver()
asyncio.run(receiver.main())
