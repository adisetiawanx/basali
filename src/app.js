import Express from "express";
import Cors from "cors";

import Config from "./config.js";
import AuthRoutes from "./routes/auth.routes.js";
import ProfileRoutes from "./routes/profile.routes.js";

const app = Express();

app.use(Cors());
app.use(Express.json());

app.get("/", (req, res) => {
  return res.json({
    msg: "Hello World",
  });
});

app.use("/api/auth", AuthRoutes);
app.use("/api/profile", ProfileRoutes);

app.listen(Config.PORT, () => {
  console.log(`Example app listening on port ${Config.PORT}`);
});
