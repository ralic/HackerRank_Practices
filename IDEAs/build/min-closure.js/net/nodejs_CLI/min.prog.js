'use strict';var program=require("commander");program.version("0.0.1").option("-C, --chdir \x3cpath\x3e","change the working directory").option("-c, --config \x3cpath\x3e","set config path. defaults to ./deploy.conf").option("-T, --no-tests","ignore test hook");
program.command("setup [env]").description("run setup commands for all envs").option("-s, --setup_mode [mode]","Which setup mode to use").action(function(a,b){var c=b.setup_mode||"normal";console.log("setup for %s env(s) with %s mode",a||"all",c)});
program.command("exec \x3ccmd\x3e").alias("ex").description("execute the given remote cmd").option("-e, --exec_mode \x3cmode\x3e","Which exec mode to use").action(function(a,b){console.log('exec "%s" using %s mode',a,b.exec_mode)}).on("--help",function(){console.log("  Examples:");console.log();console.log("    $ deploy exec sequential");console.log("    $ deploy exec async");console.log()});program.command("*").action(function(a){console.log('deploying "%s"',a)});program.parse(process.argv);