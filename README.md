# NERD VEND / FKS Vendor

## What is it?

Origin started a few years back when a friend requested a custom solution for a "one stop shop" for everything.
From hosting bots, to bloggers to simple blogging. I started the project in C#, then I migrated it to NestJS from there
to PHP using Symfony and now, we have Spring.

Back to the question, what is it? It's the place where you can sell your virtual products in Second Life. Much like
CapserVend, MDLabs and Allomancy, NerdVend (or FKSVendor) was created as a quick and easy solution for new store owners
to get up and running quickly.

## How Do?

Currently, it's being re-worked and I'm periodically adding things to it. If it seems like a dead project, it might be
but open an issue and I'll see what I can do. However, the bare minimum to get this working is ensure you
have [Docker]() installed and simply run `docker compose up -d` and the container should spin up.Run into issues?
Message me on [SL](https://www.secondlife.com/).

## Important

Change the database information in the `.env` file. If there's no `.env` make one. It should follow this format.

```yaml
APP_NAME=nerdvend
DATABASE_USER=dbuser
DATABASE_PASSWORD=dbpassword
DATABASE_NAME=dbname
```

Make sure you update them.
If you don't have the LSL scripts, again, message me in world.

## Side note

Yes, I'm
aware [application.properties](https://github.com/afallenhope/java-spring-fksvendor/blob/main/src/main/resources/application.properties)
has sensitive information... but this is just a test db running locally in docker.