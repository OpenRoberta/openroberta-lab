export const schema = {
	"$schema": "http://json-schema.org/draft-07/schema#",
	"$id": "http://open-roberta.org/common.schema.json",
	"title": "common",
	"properties": {
		"title": { "type": "string" },
		"robotGroup": {
			"type": "string",
		},
		"blocks": {
			"type": "array"
		}
	},
	"required": ["title"]

};
