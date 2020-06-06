
export class Log {
	private logPrefix = '';
	constructor(logPrefix?: string) {
		if (logPrefix) {
			this.logPrefix = `[${logPrefix}]: `;
		}
	}

	get info(): Function {
		if (!this.isValidLogLevel("info")) {
			return () => { };
		}
		const logPrefix = this.logPrefix;
		if (logPrefix.length) {
			return console.log.bind(window.console, logPrefix);
		} else {
			return console.log.bind(window.console);
		}
	}
	get log(): Function {
		if (!this.isValidLogLevel("log")) {
			return () => { };
		}
		const logPrefix = this.logPrefix;
		if (logPrefix.length) {
			return console.log.bind(window.console, logPrefix);
		} else {
			return console.log.bind(window.console);
		}
	}

	get warn(): Function {
		if (!this.isValidLogLevel("warn")) {
			return () => { };
		}
		const logPrefix = this.logPrefix;
		if (logPrefix.length) {
			return console.warn.bind(window.console, logPrefix);
		} else {
			return console.warn.bind(window.console);
		}
	}

	get error(): Function {
		if (!this.isValidLogLevel("error")) {
			return () => { };
		}
		const logPrefix = this.logPrefix;
		if (logPrefix.length) {
			return console.error.bind(window.console, logPrefix);
		} else {
			return console.error.bind(window.console);
		}
	}
	isValidLogLevel(logLevel: string): boolean {
		let log = (window as any).LOG;
		if (!log) {
			return false;
		}
		switch (log) {
			case "error":
				return logLevel === "error" ? true : false;
			case "warn":
				return logLevel === "warn" ? true : logLevel === "error" ? true : false;
			case "info":
				return logLevel === "info" ? true : logLevel === "warn" ? true : logLevel === "error" ? true : false;
			default:
				return true;
		}
	}

}